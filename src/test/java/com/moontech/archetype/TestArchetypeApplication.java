package com.moontech.archetype;

import org.springframework.boot.SpringApplication;

public class TestArchetypeApplication {

	public static void main(String[] args) {
		SpringApplication.from(ArchetypeApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
