package rabbitmq.plugin.example03

import java.util.concurrent.atomic.AtomicInteger

class PusSubTests extends GroovyTestCase {
    def rabbitTemplate
    def sub1Service
    def sub2Service

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
        sub1Service.callback = null
        sub2Service.callback = null
    }

    void testConsumeMessage() {
        def consumed = new AtomicInteger(0)

        def callback = { message ->
            println message
            consumed.incrementAndGet()
            assert 'someMessage' == message
        }

        sub1Service.callback = callback
        sub2Service.callback = callback

        // Exchange amqp.direct with routing key myQueueName
        rabbitTemplate.convertAndSend 'my.broadcast', '', 'someMessage'
        sleep(10)
        assert 2 == consumed.intValue()
    }
}
