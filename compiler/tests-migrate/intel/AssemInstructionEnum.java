package com.chaosopher.tigerlang.compiler.intel;

enum AssemInstructionEnum {
    MOVE_EXP_TO_TEMP,
    MOVE_CONST_TO_TEMP,
    LOAD_INDIRECT,
    LOAD_INDIRECT_DISP,
    LOAD_INDIRECT_DISP_SCALED, 
    STORE_INDIRECT,
    STORE_INDIRECT_DISP,
    STORE_INDIRECT_DISP_SCALED,
    BINOP,
    CALL, 
    CJUMP, 
    JUMP
}