.global tigermain
.text
L1:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L3:
movq %rdi, -8(%rbp) # store to offset 1
movq -8(%rbp), %rax # mem(boe)
movq -16(%rax), %rax # mem(boe)
movq -8(%rbp), %rcx # mem(boe)
movq -16(%rcx), %rcx # mem(boe)
imul %rcx # imul rax * r 
jmp L2
L2:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
L0:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
# start main
# start 
L5:
movq %rdi, -8(%rbp) # store to offset 1
movq %rsi, -16(%rbp) # store to offset 1
movq %rbp, %rdi # move reg arg 0 to temp
call L1
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
subq $0, %rsp
# start main
# start 
L7:
movq $10, %rsi # integerExpression
movq %rbp, %rdi # move reg arg 0 to temp
call L0
movq %rax, %rdi # rax to temp 
call printi
jmp L6
L6:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
