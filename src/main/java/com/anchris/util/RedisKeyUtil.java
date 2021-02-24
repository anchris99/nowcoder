package com.anchris.util;

public class RedisKeyUtil {
    private static final String SPLIT=":";
    private static final String PREFIX_ENTITY_LIKE="like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
    private static final String PREFIX_FOLLOWEE = "followee";
    private static final String PREFIX_FOLLOWER = "follower";

    //某个实体的赞
    public static String getEntityLikeKey(int entityType,int entityId){
        return PREFIX_ENTITY_LIKE+entityType+SPLIT+entityId;
    }
    // 某个用户的赞
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    // 实体 追随者 key, follower  (entityType:entityId  userId)
    public static String getFollowerKey(int entityType,int entityId){
        return PREFIX_FOLLOWER+SPLIT+entityType+SPLIT+entityId;
    }
    // user追随的实体 key （userId：entityType   entityId)
    public static String getFolloweeKey(int userId,int entityType){
        return PREFIX_FOLLOWEE+SPLIT+userId+SPLIT+entityType;
    }
}
