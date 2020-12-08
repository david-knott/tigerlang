package com.chaosopher.tigerlang.compiler.util;

import java.io.InputStream;
import java.io.OutputStream;

import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;

public interface TaskProvider {

	void build(TaskRegister taskRegister);
}