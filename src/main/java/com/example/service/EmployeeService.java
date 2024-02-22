package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Employee;
import com.example.repository.EmployeeRepository;

/**
 * 従業員情報を操作するサービス.
 * 
 * @author igamasayuki
 *
 */
@Service
@Transactional
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	/**
	 * 従業員情報を全件取得します.
	 * 
	 * @return 従業員情報一覧
	 */
	public List<Employee> showList() {
		List<Employee> employeeList = employeeRepository.findAll();
		return employeeList;
	}

	/**
	 * 従業員情報を取得します.
	 * 
	 * @param id ID
	 * @return 従業員情報
	 * @throws org.springframework.dao.DataAccessException 検索されない場合は例外が発生します
	 */
	public Employee showDetail(Integer id) {
		Employee employee = employeeRepository.load(id);
		return employee;
	}

	/**
	 * 従業員情報を更新します.
	 * 
	 * @param employee 更新した従業員情報
	 */
	public void update(Employee employee) {
		employeeRepository.update(employee);
	}

	// public List<Employee> search(String name){
	// 	if(name == null || name.isEmpty()) {
	// 		return employeeRepository.findAll();
	// 	}else{
	// 		return employeeRepository.search(name);
	// 	}
	// }

	public List<Employee> findAll(){
		return employeeRepository.findAll();
	}

	// public List<Employee> findEmployeesByPage(int page, int pageSize){
	// 	int offset = (page - 1) * pageSize;
	// 	return employeeRepository.findEmployeesByPage(offset, pageSize);
	// }

	public List<Employee> search(String name, int page, int pageSize){
		int offset = (page - 1) * pageSize;
		return employeeRepository.search(name, offset, pageSize);
	}

	public int countEmployees(){
		return employeeRepository.countEmployees();
	}

	public List<String> findEmployeeNamesStartingWith(String term) {
		return employeeRepository.findEmployeeNamesStartingWith(term);
	}

	public int countSearchResults(String name){
		return employeeRepository.countSearchResults(name);
	}
}
