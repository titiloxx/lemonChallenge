

/**
 * Represents a session of a user.
 *
 * @author	Agustin Albiero
 * @since	v1.0.0
 * @version	v1.0.0	Thursday, February 17th, 2022.
 */
class SessionInfo{
   /**
    * The number of queries executed within a MAX_TIME value
    */
   var count: Int = 0

   /**
    * The timestamp for the first query made within a MAX_TIME value
    */
   var timeStamp: Long = 0
}

