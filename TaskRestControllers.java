package com.SeminarWoT;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TaskRestControllers {

	private WorkFlow wf = new WorkFlow();
	
	@RequestMapping(value = "/res", method = RequestMethod.GET)
    public  String getResssource() {
       // return wf.getRessource(ipAddress);
		return "page";
    }

}
