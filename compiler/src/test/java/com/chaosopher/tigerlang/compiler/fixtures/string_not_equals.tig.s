.global tigermain
.data
L0:
	.long  0x5
	.ascii "april\0"
.data
L1:
	.long  0x5
	.ascii "david\0"
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
# start main
# start 
L3:
movq $L0, %rdi # default name
movq $L1, %rsi # default name
call stringEqual
movq %rax, %rdi # rax to temp 
call printi
jmp L2
L2:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
