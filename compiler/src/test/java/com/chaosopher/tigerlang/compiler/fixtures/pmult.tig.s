.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
# start main
# start 
L1:
movq $3, %rax # move(t, i)
movq $4, %rcx # move(t, i)
imul %rcx # imul rax * r 
movq %rax, %rdi # move reg arg 0 to temp
call printi
jmp L0
L0:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
