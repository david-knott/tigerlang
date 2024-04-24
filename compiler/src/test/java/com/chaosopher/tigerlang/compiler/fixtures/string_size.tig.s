.global tigermain
.data
L0:
	.long  0x5
	.ascii "david\0"
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
# start main
# start 
L2:
movq $L0, %rdi # default name
call size
movq %rax, %rdi # rax to temp 
call printi
jmp L1
L1:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
