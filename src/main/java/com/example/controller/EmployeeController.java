package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.domain.Employee;
import com.example.form.UpdateEmployeeForm;
import com.example.service.EmployeeService;

/**
 * 従業員情報を操作するコントローラー.
 * 
 * @author igamasayuki
 *
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public UpdateEmployeeForm setUpForm() {
		return new UpdateEmployeeForm();
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員一覧を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員一覧画面を出力します.
	 * @param page 表示するページ番号
	 * @param model モデル
	 * @return 従業員一覧画面
	 */
	// @GetMapping("/showList")
	// public String showList(@RequestParam(name = "page", defaultValue = "1") int page, Model model) {
	// 	//1ページに表示する従業員の数を設定
	// 	int pageSize = 10;
	// 	//指定ページの従業員一覧を取得
	// 	List<Employee> employeeList = employeeService.findEmployeesByPage(page, pageSize);
	// 	//ページング情報を設定
	// 	int totalEmployees = employeeService.countEmployees();
	// 	int totalPages = (int) Math.ceil((double) totalEmployees / pageSize);
	// 	int nextPage = Math.min(page + 1, totalPages);
	// 	int prevPage = Math.max(page - 1, 1);

	// 	model.addAttribute("employeeList", employeeList);
	// 	model.addAttribute("currentPage", page);
	// 	model.addAttribute("totalPages", totalPages);
	// 	model.addAttribute("nextPage", nextPage);
	// 	model.addAttribute("prevPage", prevPage);
	// 	return "employee/list";
	// }

	@GetMapping("/showList")
	public String showList(
		@RequestParam(name = "page", defaultValue = "1") int page,
		@RequestParam(value = "name", required = false) String name,
		Model model) {
		int pageSize = 10;
		List<Employee> employeeList;
		if (name != null && !name.isEmpty()) {
			employeeList = employeeService.search(name, page, pageSize);
		} else {
			employeeList = employeeService.search(name, page, pageSize);
		}

		int totalEmployees = employeeService.countSearchResults(name);
		int totalPages = (int) Math.ceil((double) totalEmployees / pageSize);
		int nextPage = Math.min(page + 1, totalPages);
		int prevPage = Math.max(page - 1, 1);

		model.addAttribute("name", name);
		model.addAttribute("employeeList", employeeList);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("nextPage", nextPage);
		model.addAttribute("prevPage", prevPage);
		return "employee/list";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細画面を出力します.
	 * 
	 * @param id    リクエストパラメータで送られてくる従業員ID
	 * @param model モデル
	 * @return 従業員詳細画面
	 */
	@GetMapping("/showDetail")
	public String showDetail(String id, Model model) {
		Employee employee = employeeService.showDetail(Integer.parseInt(id));
		model.addAttribute("employee", employee);
		return "employee/detail";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を更新する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細(ここでは扶養人数のみ)を更新します.
	 * 
	 * @param form 従業員情報用フォーム
	 * @return 従業員一覧画面へリダクレクト
	 */
	@PostMapping("/update")
	public String update(@Validated UpdateEmployeeForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return showDetail(form.getId(), model);
		}
		Employee employee = new Employee();
		employee.setId(form.getIntId());
		employee.setDependentsCount(form.getIntDependentsCount());
		employeeService.update(employee);
		return "redirect:/employee/showList";
	}

	// @GetMapping("/search")
    // public String searchEmployees(
    //     @RequestParam(value = "name", required = false) String name, Model model) {
    //     List<Employee> employees = employeeService.search(name);
	// 	if(employees.isEmpty()){
	// 		model.addAttribute("errorMessage", "１件もありませんでした");
	// 		employees = employeeService.findAll();
	// 	}

    //     model.addAttribute("employeeList", employees);

    //     return "employee/list";
    // }

	@GetMapping("/search")
	public String searchEmployees(
		@RequestParam(value = "name", required = false) String name,
		@RequestParam(name = "page", defaultValue = "1") int page,
		Model model) {
		int pageSize = 10;
		List<Employee> employees = employeeService.search(name, page, pageSize);
		if(employees.isEmpty()){
			model.addAttribute("errorMessage", "１件もありませんでした");
			employees = employeeService.findAll();
		}

		// 検索結果の総数を取得
		int totalEmployees = employeeService.countSearchResults(name);
		int totalPages = (int) Math.ceil((double) totalEmployees / pageSize);

		// pageがtotalPagesを超える場合はtotalPagesに設定する
		page = Math.min(page, totalPages);

		int nextPage = Math.min(page + 1, totalPages);
		int prevPage = Math.max(page - 1, 1);

		model.addAttribute("name", name);
		model.addAttribute("employeeList", employees);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("nextPage", nextPage);
		model.addAttribute("prevPage", prevPage);

		return "employee/list";
	}


	@GetMapping("/searchAutocomplete")
	@ResponseBody
	public List<String> searchAutocomplete(@RequestParam("term") String term) {
		return employeeService.findEmployeeNamesStartingWith(term);
	}
}
