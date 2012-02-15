package rabbitmq.plugin.example01

import grails.test.*  
import java.util.concurrent.atomic.*

class DirectExchangeTests extends GroovyTestCase { 
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
          println consumed 
          println message
          consumed = true                
          assert 'someMessage' == message
        }
      
        // Exchange amqp.direct with routing key myQueName 
        rabbitTemplate.convertAndSend '', 'myQueueName', 'someMessage'
        sleep(10)
        assert consumed
    }
    
    void testConcurrentConsumers() {   
        def consumed = new AtomicInteger(0)
        def threads = Collections.synchronizedList([])

        consumerService.callback = { message ->  
          consumed.incrementAndGet()       
          assert 'someMessage' == message
          threads << Thread.currentThread().name
        }
        
        // 5 messages round robin with acknowledgeMode5 consumers
        5.times { 
          rabbitTemplate.convertAndSend '', 'myQueueName', 'someMessage'
        }

        sleep(10)
        assert 5 == consumed.intValue()
        assert threads.unique().size() > 1 // Check more than one thread consume the queue
    }
}
