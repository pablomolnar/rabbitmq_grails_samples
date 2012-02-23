package rabbitmq.plugin.example02

import grails.test.*
import java.util.concurrent.atomic.*

class CustomExchangeTests extends GroovyTestCase {
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
        boolean consumed = false

        consumerService.callback = { message ->
            println message
            consumed = true
            assert 'someMessage' == message
        }

        // Exchange amqp.direct with routing key myQueueName
        rabbitTemplate.convertAndSend 'my.exchange.direct', 'myQueueName', 'someMessage'
        sleep(10)
        assert consumed
    }

    void testRetryMessage() {
        def consumed = new AtomicInteger(0)

        consumerService.callback = { message ->
            assert 'someMessage' == message

            // Force fail 5 times
            if(consumed.incrementAndGet() < 5) {
                throw new RuntimeException("Nr: ${consumed.intValue()}")
            }
        }

        // Exchange amqp.direct with routing key myQueueName
        rabbitTemplate.convertAndSend 'my.exchange.direct', 'myQueueName', 'someMessage'
        sleep(50)
        assert 5 == consumed.intValue()
    }
}
