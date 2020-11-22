package jp.ac.hal.userauth.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import jp.ac.hal.userauth.model.UserInfo;

public interface UserInfoMapper {

    int selectUserId(@Param("userId") int userId);

    UserInfo selectByUserNameAndPw(@Param("userName") String userName, @Param("password") String password);

    List<UserInfo> selectByUserName(@Param("userName") String userName);

    boolean insertUserinfo(UserInfo userinfo);

    boolean updateUserinfo(UserInfo userinfo);

    boolean deleteUserinfo(@Param("userId") long userId);

    boolean updateAvailabilityFlag(@Param("userId") long userId, @Param("availabilityFlag") String availabilityFlag);

    boolean updateJsessionId(@Param("userId") long userId, @Param("jsessionId") String jsessionId);
}
