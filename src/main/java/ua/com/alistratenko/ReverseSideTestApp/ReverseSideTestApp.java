package ua.com.alistratenko.ReverseSideTestApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ua.com.alistratenko.ReverseSideTestApp.service.ActionService;

@SpringBootApplication
@Import(ApplicationBeansConfiguration.class)
public class ReverseSideTestApp {

	private static ActionService service;

	@Autowired
	public ReverseSideTestApp(ActionService service) {
		ReverseSideTestApp.service = service;
	}

	public static void main(String[] args) {
		SpringApplication.run(ReverseSideTestApp.class, args);

		new Thread(() -> service.showTopFiveLikersOfSpecificGroup()).start();

		service.showSexCountOfGroup();
	}


}
