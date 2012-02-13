package rabbitmq.plugin

import grails.test.*  
import java.util.concurrent.atomic.*

class CustomExchangeTests extends GroovyTestCase { 
    def rabbitTemplate
    def customExchangeBindQueueService
    
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
        customExchangeBindQueueService.callback = null
    }

    void testConsumeMessage() {   
        boolean consumed = false

        customExchangeBindQueueService.callback = { message ->
          consumed = true                
          assert 'someMessage' == message
        }
      
        // Exchange amqp.direct with routing key myQueName 
        rabbitTemplate.convertAndSend 'my.exchange.direct', 'customExchangeBindQueue', 'someMessage'
        sleep(10)
        assert consumed
    }
}
