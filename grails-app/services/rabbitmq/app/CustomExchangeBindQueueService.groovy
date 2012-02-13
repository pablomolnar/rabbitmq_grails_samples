package rabbitmq.app

class CustomExchangeBindQueueService {

  static transactional = false
  static rabbitQueue = 'customExchangeBindQueue'
  
  Closure callback

  void handleMessage(message) {                
    if(callback) {
      callback(message)  
    } else {
      println message
    }
  }
}
