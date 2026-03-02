package com.dcspa.prism.cotroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "test")
public class MonController {

	@GetMapping(path = "string")
	public String getString() {
		return "Retour de ma methode";
	}
}


