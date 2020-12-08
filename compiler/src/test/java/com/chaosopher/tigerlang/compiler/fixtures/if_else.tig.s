.global tigermain
.data
L0:
	.long  0x4
	.ascii "then\0"
.data
L1:
	.long  0x4
	.ascii "else\0"
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
# start main
# start 
L6:
movq $2, %rax # move(t, i)
movq $2, %rcx # integerExpression
cmp %rcx, %rax
je L2
L3:
movq $L1, %rdi # default name
call print
L4:
jmp L5
L2:
movq $L0, %rdi # default name
call print
jmp L4
L5:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
