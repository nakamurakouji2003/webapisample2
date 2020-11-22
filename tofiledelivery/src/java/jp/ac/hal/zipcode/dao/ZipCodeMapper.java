package jp.ac.hal.zipcode.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import jp.ac.hal.zipcode.model.ZipCode;

public interface ZipCodeMapper {

	List<ZipCode> selectByZipCode(@Param("zipcode") String zipcode);

}
