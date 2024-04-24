.global tigermain
.text
L0:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L3:
movq %rdi, -8(%rbp) # store to offset 1
movq -8(%rbp), %rax # mem(boe)
movq -8(%rax), %rdi # mem(boe)
call printi
jmp L2
L2:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
L1:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L5:
movq %rdi, -8(%rbp) # store to offset 1
movq -8(%rbp), %rdi # mem(boe)
call L0
jmp L4
L4:
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
L7:
movq $3, -8(%rbp) # store to offset 2
movq %rbp, %rdi # move reg arg 0 to temp
call L1
jmp L6
L6:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
