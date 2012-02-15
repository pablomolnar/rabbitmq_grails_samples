package rabbitmq.plugin.example05

import org.springframework.amqp.core.MessagePostProcessor
import org.springframework.amqp.core.Message
import java.util.concurrent.atomic.AtomicInteger

class HeadersRoutingTests extends GroovyTestCase {
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
        }
      
        // Exchange amqp.direct with routing key myQueName
        10.times{
            rabbitTemplate.convertAndSend 'amq.headers', '', "$it".toString(), new MessagePostProcessor(){
                Message postProcessMessage(Message message) {
                    def isPrime = new BigInteger(message.getBodyContentAsString()).isProbablePrime(100)
                    message.messageProperties.headers.prime = isPrime
                    message
                }
            }
        }
        sleep(10)
        assert 4 == consumed.intValue()
    }
}
