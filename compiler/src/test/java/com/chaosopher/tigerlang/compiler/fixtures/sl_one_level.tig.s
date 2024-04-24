.global tigermain
.text
L0:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L2:
movq %rdi, -8(%rbp) # store to offset 1
movq -8(%rbp), %rax # mem(boe)
movq -8(%rax), %rdi # mem(boe)
call printi
jmp L1
L1:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L4:
movq $3, -8(%rbp) # store to offset 2
movq %rbp, %rdi # move reg arg 0 to temp
call L0
jmp L3
L3:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
