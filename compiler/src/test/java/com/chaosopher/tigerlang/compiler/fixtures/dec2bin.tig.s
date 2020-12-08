.global tigermain
.text
L0:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
# start main
# start 
L14:
movq %rdi, -8(%rbp) # store to offset 1
movq %rsi, -16(%rbp) # spill store
movq $0, %rcx # integerExpression
movq -16(%rbp), %rsi # spill load
cmp %rcx, %rsi
jg L2
L3:
L4:
jmp L13
L2:
movq -8(%rbp), %rdi # mem(boe)
movq $2, %rcx # bin(t, i)
movq -16(%rbp), %rsi # spill load
movq %rsi, %rax # div r -> rax
xor %rdx, %rdx # div clear bits rdx 
idiv %rcx # div rax * rexp 
movq %rax, %rsi # move reg arg 1 to temp
call L0
movq $2, %rcx # bin(t, i)
movq -16(%rbp), %rsi # spill load
movq %rsi, %rax # div r -> rax
xor %rdx, %rdx # div clear bits rdx 
idiv %rcx # div rax * rexp 
movq $2, %rcx # bin(t, i)
imul %rcx # imul rax * r 
movq -16(%rbp), %rsi # spill load
sub %rax, %rsi
movq %rsi, %rdi # move reg arg 0 to temp
call printi
jmp L4
L13:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L5:
	.long  0x6
	.ascii "\t->\t\0"
.data
L6:
	.long  0x2
	.ascii "\n\0"
.data
L7:
	.long  0x6
	.ascii "\t->\t\0"
.data
L8:
	.long  0x2
	.ascii "\n\0"
.data
L9:
	.long  0x6
	.ascii "\t->\t\0"
.data
L10:
	.long  0x2
	.ascii "\n\0"
.data
L11:
	.long  0x6
	.ascii "\t->\t\0"
.data
L12:
	.long  0x2
	.ascii "\n\0"
.text
L1:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L16:
movq %rdi, -8(%rbp) # store to offset 1
movq $100, %rdi # integerExpression
call printi
movq $L5, %rdi # default name
call print
movq -8(%rbp), %rdi # mem(boe)
movq $100, %rsi # integerExpression
call L0
movq $L6, %rdi # default name
call print
movq $200, %rdi # integerExpression
call printi
movq $L7, %rdi # default name
call print
movq -8(%rbp), %rdi # mem(boe)
movq $200, %rsi # integerExpression
call L0
movq $L8, %rdi # default name
call print
movq $789, %rdi # integerExpression
call printi
movq $L9, %rdi # default name
call print
movq -8(%rbp), %rdi # mem(boe)
movq $789, %rsi # integerExpression
call L0
movq $L10, %rdi # default name
call print
movq $567, %rdi # integerExpression
call printi
movq $L11, %rdi # default name
call print
movq -8(%rbp), %rdi # mem(boe)
movq $567, %rsi # integerExpression
call L0
movq $L12, %rdi # default name
call print
jmp L15
L15:
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
L18:
movq %rbp, %rdi # move reg arg 0 to temp
call L1
jmp L17
L17:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
