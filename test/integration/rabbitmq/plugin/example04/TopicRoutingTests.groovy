package rabbitmq.plugin.example04

import java.util.concurrent.atomic.AtomicInteger
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessagePostProcessor

class TopicRoutingTests extends GroovyTestCase {
    def rabbitTemplate
    def consumerService
    
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
        consumerService.callback = null
    }

    void testConsumeMessage() {   
        def consumed = new AtomicInteger(0)


        consumerService.callback = { message ->
          consumed.incrementAndGet()
          assert 'somePrime' == message
        }
      
        // Exchange amqp.direct with routing key myQueName
        10.times{
            def routingKey = ''
            def isPrime = new BigInteger(it.toString()).isProbablePrime(100)
            if(isPrime) {
                routingKey = 'prime'
            }

            rabbitTemplate.convertAndSend 'my.topic', routingKey, "somePrime".toString()
        }
        sleep(10)
        assert 4 == consumed.intValue()
    }
}
