.global tigermain
.text
L0:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L21:
movq %rdi, -8(%rbp) # store to offset 1
movq $1, %rcx # move(t, i)
movq $2, %r8 # move(t, i)
movq $2, %rdi # bin(t, i)
movq %rsi, %rax # div r -> rax
xor %rdx, %rdx # div clear bits rdx 
idiv %rdi # div rax * rexp 
movq %rax, %rdi # move(t, t)
L2:
cmp %rdi, %r8
jle L3
L5:
movq %rcx, %rax # move(t, t)
jmp L20
L3:
movq %rsi, %rax # div r -> rax
xor %rdx, %rdx # div clear bits rdx 
idiv %r8 # div rax * rexp 
imul %r8 # imul rax * r 
cmp %rsi, %rax
je L6
L7:
L8:
cmp %rdi, %r8
je L5
L4:
movq $1, %rax # bin(t, i)
add %rax, %r8
jmp L2
L6:
movq $0, %rcx # move(t, i)
jmp L5
L22:
jmp L8
L20:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L9:
	.long  0x2
	.ascii "\n\0"
.data
L10:
	.long  0x2
	.ascii "\n\0"
.data
L11:
	.long  0x2
	.ascii "\n\0"
.data
L12:
	.long  0x2
	.ascii "\n\0"
.data
L13:
	.long  0x2
	.ascii "\n\0"
.data
L14:
	.long  0x2
	.ascii "\n\0"
.data
L15:
	.long  0x2
	.ascii "\n\0"
.data
L16:
	.long  0x2
	.ascii "\n\0"
.data
L17:
	.long  0x2
	.ascii "\n\0"
.data
L18:
	.long  0x2
	.ascii "\n\0"
.data
L19:
	.long  0x2
	.ascii "\n\0"
.text
L1:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L24:
movq %rdi, -8(%rbp) # store to offset 1
movq -8(%rbp), %rdi # mem(boe)
movq $56, %rsi # integerExpression
call L0
movq %rax, %rdi # rax to temp 
call printi
movq $L9, %rdi # default name
call print
movq -8(%rbp), %rdi # mem(boe)
movq $23, %rsi # integerExpression
call L0
movq %rax, %rdi # move reg arg 0 to temp
call printi
movq $L10, %rdi # default name
call print
movq -8(%rbp), %rdi # mem(boe)
movq $71, %rsi # integerExpression
call L0
movq %rax, %rdi # rax to temp 
call printi
movq $L11, %rdi # default name
call print
movq -8(%rbp), %rdi # mem(boe)
movq $72, %rsi # integerExpression
call L0
movq %rax, %rdi # move reg arg 0 to temp
call printi
movq $L12, %rdi # default name
call print
movq -8(%rbp), %rdi # mem(boe)
movq $173, %rsi # integerExpression
call L0
movq %rax, %rdi # rax to temp 
call printi
movq $L13, %rdi # default name
call print
movq -8(%rbp), %rdi # mem(boe)
movq $181, %rsi # integerExpression
call L0
movq %rax, %rdi # rax to temp 
call printi
movq $L14, %rdi # default name
call print
movq -8(%rbp), %rdi # mem(boe)
movq $281, %rsi # integerExpression
call L0
movq %rax, %rdi # move reg arg 0 to temp
call printi
movq $L15, %rdi # default name
call print
movq -8(%rbp), %rdi # mem(boe)
movq $659, %rsi # integerExpression
call L0
movq %rax, %rdi # move reg arg 0 to temp
call printi
movq $L16, %rdi # default name
call print
movq -8(%rbp), %rdi # mem(boe)
movq $729, %rsi # integerExpression
call L0
movq %rax, %rdi # move reg arg 0 to temp
call printi
movq $L17, %rdi # default name
call print
movq -8(%rbp), %rdi # mem(boe)
movq $947, %rsi # integerExpression
call L0
movq %rax, %rdi # move reg arg 0 to temp
call printi
movq $L18, %rdi # default name
call print
movq -8(%rbp), %rdi # mem(boe)
movq $945, %rsi # integerExpression
call L0
movq %rax, %rdi # move reg arg 0 to temp
call printi
movq $L19, %rdi # default name
call print
jmp L23
L23:
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
L26:
movq %rbp, %rdi # move reg arg 0 to temp
call L1
jmp L25
L25:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
