.global tigermain
.text
L0:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L4:
movq %rdi, -8(%rbp) # store to offset 1
movq -8(%rbp), %rax # mem(boe)
movq -8(%rax), %rdi # mem(boe)
call printi
jmp L3
L3:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
L2:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L6:
movq %rdi, -8(%rbp) # store to offset 1
movq -8(%rbp), %rax # mem(boe)
movq -8(%rax), %rdi # mem(boe)
call L0
jmp L5
L5:
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
L8:
movq %rdi, -8(%rbp) # store to offset 1
movq %rbp, %rdi # move reg arg 0 to temp
call L2
jmp L7
L7:
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
L10:
movq $3, -8(%rbp) # store to offset 2
movq %rbp, %rdi # move reg arg 0 to temp
call L1
jmp L9
L9:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
