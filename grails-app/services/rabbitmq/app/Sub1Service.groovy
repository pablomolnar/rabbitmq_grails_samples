package rabbitmq.app

class Sub1Service {

    static transactional = false
    static rabbitSubscribe = 'my.broadcast'
    
    Closure callback

    void handleMessage(String message) {
      if(callback) {
        callback(message)  
      } else {
        println message
      }
    } 
}
