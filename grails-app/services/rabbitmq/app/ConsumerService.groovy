package rabbitmq.app

class ConsumerService {

    static transactional = true
    static rabbitQueue = 'myQueueName'

    void handleMessage(message) {
        def thread = Thread.currentThread().name
        println "Recieved: $message in thread $thread"
    } 
      
    
}
