package rabbitmq.app

class ConsumerService {

    static transactional = false
    static rabbitQueue = 'myQueueName'
    
    Closure callback

    void handleMessage(message) {
      if(callback) {
        callback(message)  
      } else {
        println message
      }
    } 
}
