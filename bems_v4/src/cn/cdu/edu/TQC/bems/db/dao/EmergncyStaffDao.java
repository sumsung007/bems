/**
* @Copyright (c) 成都大学信息科学与技术学院
* 服务外包与创意大赛
*/
package cn.cdu.edu.TQC.bems.db.dao;

import java.util.List;

import cn.cdu.edu.TQC.bems.db.bean.EmergncyStaff;

/**
 * @ClassName: EmergncyStaffDao
 * 说明：
 * TODO(Tell the reader such role.)
 * @Author SUN	【email:1096490965@qq.com】
 * @Version V1.0	2012-7-8 下午8:12:43
 *
 */
public interface EmergncyStaffDao {
	
	public boolean addEmergncyStaff(EmergncyStaff emergncyStaff);
	
	public boolean updateEmergncyStaff(EmergncyStaff emergncyStaff);
	
	public EmergncyStaff getEmergncyStaffById(EmergncyStaff emergncyStaff);
	
	public List<EmergncyStaff> getEmergncyStaffs();

	public boolean deleteEmergncyStaff(Integer emergncyStaffId);
	public boolean deleteEmergncyStaffByStaffId(String staffId);
}
