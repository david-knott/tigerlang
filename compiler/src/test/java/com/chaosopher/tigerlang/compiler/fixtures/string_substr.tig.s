.global tigermain
.data
L0:
	.long  0x7
	.ascii "brillig\0"
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
# start main
# start 
L2:
movq $L0, %rdi # default name
movq $2, %rsi # integerExpression
movq $5, %rdx # integerExpression
call substring
movq %rax, %rdi # move reg arg 0 to temp
call print
jmp L1
L1:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
