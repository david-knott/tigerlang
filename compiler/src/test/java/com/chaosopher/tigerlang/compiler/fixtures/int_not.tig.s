.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
# start main
# start 
L1:
movq $1, %rdi # integerExpression
call not
movq %rax, %rdi # rax to temp 
call printi
jmp L0
L0:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
