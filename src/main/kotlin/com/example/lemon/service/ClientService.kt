import java.net.URL

object ClientService {
    private val counter = mutableMapOf<String,SessionCounter>()
    private val foaasURL:String = System.getenv("FOAAS_URL") ?: "https://www.foaas.com/this/Agustin"
    private val maxTime:Int=System.getenv("MAX_TIME")?.toInt() ?: 10000
    private val maxRetries:Int=System.getenv("MAX_RETRIES")?.toInt() ?: 5

    fun createIfNotExist(userId: String){
        if(!counter.containsKey(userId)){
            counter[userId] = SessionCounter()
        }
    }
    
    private fun canFetch(userId:String):Boolean{
        createIfNotExist(userId)
        val currentTimeStamp = System.currentTimeMillis()
        val userTimeStamp:Long= counter[userId]?.timeStamp ?: 0L
        val isLessThan10sec=currentTimeStamp.minus(userTimeStamp) < maxTime

        if(isLessThan10sec && counter[userId]!!.count==maxRetries){
            return false
        }
        else if(!isLessThan10sec){
            counter[userId]!!.count=0
            counter[userId]!!.timeStamp=currentTimeStamp
        }

        counter[userId]!!.count++
        return true
    }

     fun getFoaasResponse(userId: String): String {
        if (!canFetch(userId)) {
            val timeLeft = (maxTime - System.currentTimeMillis().minus(counter[userId]!!.timeStamp!!)) / 1000
            //return time left
            throw Exception("You can only fetch five times every 10 seconds. You have $timeLeft seconds left")
        }

        return URL(foaasURL).readText()
    }
}

