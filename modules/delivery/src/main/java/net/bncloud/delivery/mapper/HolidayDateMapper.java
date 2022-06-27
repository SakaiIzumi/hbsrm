package net.bncloud.delivery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.bncloud.delivery.entity.HolidayDate;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/05/19
 **/
public interface HolidayDateMapper extends BaseMapper<HolidayDate> {

    /**
     * 真实删除数据
     * @param year
     * @return
     */
    @Delete("delete from t_holiday_date where year = #{year}")
    int deleteWithYear(@Param("year") Integer year);
}
