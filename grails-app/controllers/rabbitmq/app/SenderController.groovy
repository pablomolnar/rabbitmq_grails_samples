package rabbitmq.app

class SenderController {  
   def rabbitTemplate

    def index = {   
      println 'Sending message'
      rabbitTemplate.convertAndSend '', 'myQueueName', 'someMessage'          
      
      render 'ok'
    }
}
