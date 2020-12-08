.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
# start main
# start 
L1:
movq $0, %rdi # bin(i,i)
movq $6, %rax # bin(i,i)
sub %rax, %rdi
movq $4, %rax # move(t, i)
add %rax, %rdi
call printi
jmp L0
L0:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
