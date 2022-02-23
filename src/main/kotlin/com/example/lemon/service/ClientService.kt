import java.net.URL

/**
 * ClientService is a service that provides access to the FOAAS API.
 */
object ClientService {

    /**
     *FOAAS_URL is the base URL for the FOAAS API
     */
     val FOAAS_URL: String = System.getenv("FOAAS_URL") ?: "https://www.foaas.com/this/Agustin"

    /**
     *MAX_TIME is the maximum time to wait until session counter expires
     */
     val MAX_TIME: Long = System.getenv("MAX_TIME")?.toLong() ?: 10000L

    /**
     *MAX_RETRIES is the maximum number of queries to make within the MAX_TIME
     */
     val MAX_RETRIES: Int = System.getenv("MAX_RETRIES")?.toInt() ?: 5

    /** 
    * Sessions are used to keep track of the current state of each user
    */
    private val sessionsMap = mutableMapOf<String,SessionInfo>()

    /**
     * Create a new session if not exists
     *
     * @author	Agustin Albiero
     * @since	v1.0.0
     * @version	v1.0.0	Thursday, February 17th, 2022.
     * @param   userId the user id
     * @return	void
     */
    fun createIfNotExist(userId: String){
        if(!sessionsMap.containsKey(userId)){
            sessionsMap[userId] = SessionInfo()
        }
    }
    
    /**
     * Tells if the user is allowed to make a query
     *
     * @author	Agustin Albiero
     * @since	v1.0.0
     * @version	v1.0.0	Thursday, February 17th, 2022.
     * @access	private
     * @param   userId the user id
     * @return false if the user has reached the max number of retries or the max time has been reached
     */
    private fun canFetch(userId: String): Boolean{
        createIfNotExist(userId)
        val currentTimeStamp = System.currentTimeMillis()
        val userTimeStamp: Long = sessionsMap[userId]?.timeStamp ?: 0L
        val isLessThan10sec = currentTimeStamp.minus(userTimeStamp) < MAX_TIME

        if(isLessThan10sec && sessionsMap[userId]!!.count == MAX_RETRIES){
            return false
        }
        else if(!isLessThan10sec){
            sessionsMap[userId]!!.count = 0
            sessionsMap[userId]!!.timeStamp = currentTimeStamp
        }

        sessionsMap[userId]!!.count++
        return true
    }

     /**
      * Fetches a message from FOAAS API only if allowed
      *
      * @author	 Agustin Albiero
      * @since	 v1.0.0
      * @version v1.0.0	Thursday, February 17th, 2022.
      * @param userId the user id
      * @return the response from the FOAAS service
      *
      * @throws Exception if the user is not allowed to make a query
      */
     fun getFoaasResponse(userId: String): String {
        if (!canFetch(userId)) {
            val timeLeft = (MAX_TIME - System.currentTimeMillis().minus(sessionsMap[userId]!!.timeStamp!!)) / 1000
            throw Exception("You can only fetch five times every 10 seconds. You have $timeLeft seconds left")
        }

        return URL(FOAAS_URL).readText()
    }
}

