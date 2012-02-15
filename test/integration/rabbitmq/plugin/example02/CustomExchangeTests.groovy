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
}
