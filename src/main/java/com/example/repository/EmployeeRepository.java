package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;


import com.example.domain.Employee;

/**
 * employeesテーブルを操作するリポジトリ.
 * 
 * @author igamasayuki
 * 
 */
@Repository
public class EmployeeRepository {

	/**
	 * Employeeオブジェクトを生成するローマッパー.
	 */
	private static final RowMapper<Employee> EMPLOYEE_ROW_MAPPER = (rs, i) -> {
		Employee employee = new Employee();
		employee.setId(rs.getInt("id"));
		employee.setName(rs.getString("name"));
		employee.setImage(rs.getString("image"));
		employee.setGender(rs.getString("gender"));
		employee.setHireDate(rs.getDate("hire_date"));
		employee.setMailAddress(rs.getString("mail_address"));
		employee.setZipCode(rs.getString("zip_code"));
		employee.setAddress(rs.getString("address"));
		employee.setTelephone(rs.getString("telephone"));
		employee.setSalary(rs.getInt("salary"));
		employee.setCharacteristics(rs.getString("characteristics"));
		employee.setDependentsCount(rs.getInt("dependents_count"));
		return employee;
	};

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * 従業員一覧情報を入社日順で取得します.
	 * 
	 * @return 全従業員一覧 従業員が存在しない場合はサイズ0件の従業員一覧を返します
	 */
	public List<Employee> findAll() {
		String sql = """
					SELECT * 
					FROM employees 
					ORDER BY hire_date DESC;
					""";

		List<Employee> developmentList = template.query(sql, EMPLOYEE_ROW_MAPPER);

		return developmentList;
	}

	/**
	 * 主キーから従業員情報を取得します.
	 * 
	 * @param id 検索したい従業員ID
	 * @return 検索された従業員情報
	 * @exception org.springframework.dao.DataAccessException 従業員が存在しない場合は例外を発生します
	 */
	public Employee load(Integer id) {
		String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees WHERE id=:id";

		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);

		Employee development = template.queryForObject(sql, param, EMPLOYEE_ROW_MAPPER);

		return development;
	}

	/**
	 * 従業員情報を変更します.
	 */
	public void update(Employee employee) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(employee);

		String updateSql = "UPDATE employees SET dependents_count=:dependentsCount WHERE id=:id";
		template.update(updateSql, param);
	}

    // public List<Employee> search(String name) {
    //     MapSqlParameterSource param = new MapSqlParameterSource();
    //     StringBuilder sql = new StringBuilder("SELECT * FROM employees WHERE 1 = 1 ");

    //     if (name != null) {
    //         sql.append("AND name LIKE :name ");
    //         param.addValue("name", "%" + name + "%");
    //     }

    //     List<Employee> employees = template.query(sql.toString(), param, EMPLOYEE_ROW_MAPPER);

	// 	return employees;
	// }

	// public List<Employee> findEmployeesByPage(int offset, int pageSize) {
	// 	String sql = """
	// 				SELECT * 
	// 				FROM employees 
	// 				ORDER BY hire_date DESC 
	// 				LIMIT :pageSize OFFSET :offset;
	// 				""";

	// 	MapSqlParameterSource param = new MapSqlParameterSource()
	// 		.addValue("pageSize", pageSize)
	// 		.addValue("offset", offset);

	// 	List<Employee> employees = template.query(sql, param, EMPLOYEE_ROW_MAPPER);

	// 	return employees;
	// }

	public List<Employee> search(String name, int offset, int pageSize) {
		MapSqlParameterSource param = new MapSqlParameterSource();
		StringBuilder sql = new StringBuilder("SELECT * FROM employees WHERE 1 = 1 ");
	
		if (name != null) {
			sql.append("AND name LIKE :name ");
			param.addValue("name", "%" + name + "%");
		}
	
		sql.append("ORDER BY hire_date DESC LIMIT :pageSize OFFSET :offset");
		param.addValue("pageSize", pageSize);
		param.addValue("offset", offset);
	
		List<Employee> employees = template.query(sql.toString(), param, EMPLOYEE_ROW_MAPPER);
	
		return employees;
	}

	public List<Employee> search(int offset, int pageSize) {
		MapSqlParameterSource param = new MapSqlParameterSource();
		StringBuilder sql = new StringBuilder("SELECT * FROM employees WHERE 1 = 1 ");

		sql.append("ORDER BY hire_date DESC LIMIT :pageSize OFFSET :offset");
		param.addValue("pageSize", pageSize);
		param.addValue("offset", offset);
	
		List<Employee> employees = template.query(sql.toString(), param, EMPLOYEE_ROW_MAPPER);
	
		return employees;
	}

	public int countEmployees() {
		String sql = "SELECT COUNT(*) FROM employees";
		Integer count = template.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
		return count;
	}

	public List<String> findEmployeeNamesStartingWith(String term) {
		String sql = "SELECT name FROM employees WHERE name LIKE :term";
		SqlParameterSource param = new MapSqlParameterSource().addValue("term", term + "%");
		return template.queryForList(sql, param, String.class);
	}

	public int countSearchResults(String name) {
		MapSqlParameterSource param = new MapSqlParameterSource();
		StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM employees WHERE 1 = 1 ");
	
		if (name != null) {
			sql.append("AND name LIKE :name ");
			param.addValue("name", "%" + name + "%");
		}
	
		Integer count = template.queryForObject(sql.toString(), param, Integer.class);
	
		return count;
	}
}
