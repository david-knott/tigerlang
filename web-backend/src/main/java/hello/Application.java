package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chaosopher.tigerlang.compiler.sugar.Desugar;
import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.parse.CupParser;
import com.chaosopher.tigerlang.compiler.parse.Parser;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;
import com.chaosopher.tigerlang.compiler.parse.Program;


@SpringBootApplication
@RestController
public class Application {

	private ParserService parserService;

	public Application() {
		parserService = new ParserService(new ParserFactory());
	}

	@GetMapping("/")
	public Greeting greet(@RequestParam(value = "name", defaultValue = "David") String name) {
		ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = this.parserService.parse("\"foo\" = \"bar\"", errorMsg);
        program.accept(new Binder(errorMsg));
        Desugar absynCloner = new Desugar();
		program.accept(absynCloner);
		return new Greeting(1, name);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
