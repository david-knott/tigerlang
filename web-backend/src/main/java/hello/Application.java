package hello;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

import com.chaosopher.tigerlang.compiler.canon.CanonicalizationImpl;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;
import com.chaosopher.tigerlang.compiler.regalloc.RegAllocFactory;
import com.chaosopher.tigerlang.compiler.util.TaskRegister;
import com.chaosopher.tigerlang.compiler.util.Timer;


@SpringBootApplication
@RestController
public class Application {

	private ParserService parserService;

	public Application() {
		parserService = new ParserService(new ParserFactory());
	}

	@PostMapping("/compile")
	public CompilerResponse compile(@RequestBody TigerSource tigerSource) {
		String[] args = new String[] {"--reg-alloc", "--escapes-compute", "--demove"};
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(baos);
        InputStream in = new ByteArrayInputStream(tigerSource.getCode().getBytes());
		PrintStream err = new PrintStream(new ByteArrayOutputStream());
        ErrorMsg errorMsg = new ErrorMsg("", err);
		new TaskRegister()
		.register(new com.chaosopher.tigerlang.compiler.main.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.parse.Tasks(new ParserService(new ParserFactory())))
		.register(new com.chaosopher.tigerlang.compiler.cloner.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.callgraph.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.liveness.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.inlining.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.sugar.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.bind.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.findescape.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.absyn.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.types.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.translate.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.canon.Tasks(new CanonicalizationImpl()))
		.register(new com.chaosopher.tigerlang.compiler.intel.Tasks(null, null))
		.register(new com.chaosopher.tigerlang.compiler.regalloc.Tasks(new RegAllocFactory()))
		.parseArgs(args)
		.execute(in, out, err, errorMsg);
		return new CompilerResponse(baos.toString());
	}

	@GetMapping("/")
	public String greet(@RequestParam(value = "name", defaultValue = "David") String name) {
		String[] args = new String[] {"--reg-alloc", "--escapes-compute", "--demove"};
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(baos);
        InputStream in = new ByteArrayInputStream("1".getBytes());
		PrintStream err = new PrintStream(new ByteArrayOutputStream());
        ErrorMsg errorMsg = new ErrorMsg("", err);
		new TaskRegister()
		.register(new com.chaosopher.tigerlang.compiler.main.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.parse.Tasks(new ParserService(new ParserFactory())))
		.register(new com.chaosopher.tigerlang.compiler.cloner.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.callgraph.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.liveness.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.inlining.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.sugar.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.bind.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.findescape.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.absyn.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.types.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.translate.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.canon.Tasks(new CanonicalizationImpl()))
		.register(new com.chaosopher.tigerlang.compiler.intel.Tasks(null, null))
		.register(new com.chaosopher.tigerlang.compiler.regalloc.Tasks(new RegAllocFactory()))
		.parseArgs(args)
		.execute(in, out, err, errorMsg);
		return baos.toString();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
