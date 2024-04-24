	.file	"runtime.c"
	.text
.Ltext0:
	.globl	initArray
	.type	initArray, @function
initArray:
.LFB5:
	.file 1 "runtime.c"
	.loc 1 5 0
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$32, %rsp
	movl	%edi, -20(%rbp)
	movl	%esi, -24(%rbp)
	.loc 1 7 0
	movl	-20(%rbp), %eax
	cltq
	salq	$3, %rax
	movq	%rax, %rdi
	call	malloc@PLT
	movq	%rax, -8(%rbp)
	.loc 1 8 0
	movl	$0, -12(%rbp)
	jmp	.L2
.L3:
	.loc 1 9 0 discriminator 3
	movl	-12(%rbp), %eax
	cltq
	leaq	0(,%rax,8), %rdx
	movq	-8(%rbp), %rax
	addq	%rax, %rdx
	movl	-24(%rbp), %eax
	cltq
	movq	%rax, (%rdx)
	.loc 1 8 0 discriminator 3
	addl	$1, -12(%rbp)
.L2:
	.loc 1 8 0 is_stmt 0 discriminator 1
	movl	-12(%rbp), %eax
	cmpl	-20(%rbp), %eax
	jl	.L3
	.loc 1 10 0 is_stmt 1
	movq	-8(%rbp), %rax
	.loc 1 11 0
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE5:
	.size	initArray, .-initArray
	.globl	initArrayBoundsChecked
	.type	initArrayBoundsChecked, @function
initArrayBoundsChecked:
.LFB6:
	.loc 1 14 0
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$32, %rsp
	movl	%edi, -20(%rbp)
	movl	%esi, -24(%rbp)
	.loc 1 16 0
	movl	-20(%rbp), %eax
	cltq
	salq	$3, %rax
	movq	%rax, %rdi
	call	malloc@PLT
	movq	%rax, -8(%rbp)
	.loc 1 17 0
	movl	-20(%rbp), %eax
	subl	$1, %eax
	movslq	%eax, %rdx
	movq	-8(%rbp), %rax
	movq	%rdx, (%rax)
	.loc 1 18 0
	movl	$1, -12(%rbp)
	jmp	.L6
.L7:
	.loc 1 19 0 discriminator 3
	movl	-12(%rbp), %eax
	cltq
	leaq	0(,%rax,8), %rdx
	movq	-8(%rbp), %rax
	addq	%rax, %rdx
	movl	-24(%rbp), %eax
	cltq
	movq	%rax, (%rdx)
	.loc 1 18 0 discriminator 3
	addl	$1, -12(%rbp)
.L6:
	.loc 1 18 0 is_stmt 0 discriminator 1
	movl	-12(%rbp), %eax
	cmpl	-20(%rbp), %eax
	jl	.L7
	.loc 1 20 0 is_stmt 1
	movq	-8(%rbp), %rax
	.loc 1 21 0
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE6:
	.size	initArrayBoundsChecked, .-initArrayBoundsChecked
	.globl	initRecord
	.type	initRecord, @function
initRecord:
.LFB7:
	.loc 1 25 0
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$48, %rsp
	movl	%edi, -36(%rbp)
	.loc 1 28 0
	movl	-36(%rbp), %eax
	cltq
	movq	%rax, %rdi
	call	malloc@PLT
	movq	%rax, -8(%rbp)
	movq	-8(%rbp), %rax
	movq	%rax, -16(%rbp)
	.loc 1 29 0
	movl	$0, -20(%rbp)
	jmp	.L10
.L11:
	.loc 1 30 0 discriminator 3
	movq	-16(%rbp), %rax
	leaq	8(%rax), %rdx
	movq	%rdx, -16(%rbp)
	movq	$0, (%rax)
	.loc 1 29 0 discriminator 3
	movl	-20(%rbp), %eax
	addl	$8, %eax
	movl	%eax, -20(%rbp)
.L10:
	.loc 1 29 0 is_stmt 0 discriminator 1
	movl	-20(%rbp), %eax
	cmpl	-36(%rbp), %eax
	jl	.L11
	.loc 1 31 0 is_stmt 1
	movq	-8(%rbp), %rax
	.loc 1 32 0
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE7:
	.size	initRecord, .-initRecord
	.globl	streq
	.type	streq, @function
streq:
.LFB8:
	.loc 1 41 0
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	movq	%rdi, -24(%rbp)
	movq	%rsi, -32(%rbp)
	.loc 1 43 0
	movq	-24(%rbp), %rax
	cmpq	-32(%rbp), %rax
	jne	.L14
	.loc 1 44 0
	movl	$1, %eax
	jmp	.L15
.L14:
	.loc 1 45 0
	movq	-24(%rbp), %rax
	movl	(%rax), %edx
	movq	-32(%rbp), %rax
	movl	(%rax), %eax
	cmpl	%eax, %edx
	je	.L16
	.loc 1 46 0
	movl	$0, %eax
	jmp	.L15
.L16:
	.loc 1 47 0
	movl	$0, -4(%rbp)
	jmp	.L17
.L19:
	.loc 1 48 0
	movq	-24(%rbp), %rdx
	movl	-4(%rbp), %eax
	cltq
	movzbl	4(%rdx,%rax), %ecx
	movq	-32(%rbp), %rdx
	movl	-4(%rbp), %eax
	cltq
	movzbl	4(%rdx,%rax), %eax
	cmpb	%al, %cl
	je	.L18
	.loc 1 49 0
	movl	$0, %eax
	jmp	.L15
.L18:
	.loc 1 47 0 discriminator 2
	addl	$1, -4(%rbp)
.L17:
	.loc 1 47 0 is_stmt 0 discriminator 1
	movq	-24(%rbp), %rax
	movl	(%rax), %eax
	cmpl	%eax, -4(%rbp)
	jl	.L19
	.loc 1 50 0 is_stmt 1
	movl	$1, %eax
.L15:
	.loc 1 51 0
	popq	%rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE8:
	.size	streq, .-streq
	.globl	stringEqual
	.type	stringEqual, @function
stringEqual:
.LFB9:
	.loc 1 54 0
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	movq	%rdi, -24(%rbp)
	movq	%rsi, -32(%rbp)
	.loc 1 56 0
	movq	-24(%rbp), %rax
	cmpq	-32(%rbp), %rax
	jne	.L21
	.loc 1 57 0
	movl	$1, %eax
	jmp	.L22
.L21:
	.loc 1 58 0
	movq	-24(%rbp), %rax
	movl	(%rax), %edx
	movq	-32(%rbp), %rax
	movl	(%rax), %eax
	cmpl	%eax, %edx
	je	.L23
	.loc 1 59 0
	movl	$0, %eax
	jmp	.L22
.L23:
	.loc 1 60 0
	movl	$0, -4(%rbp)
	jmp	.L24
.L26:
	.loc 1 61 0
	movq	-24(%rbp), %rdx
	movl	-4(%rbp), %eax
	cltq
	movzbl	4(%rdx,%rax), %ecx
	movq	-32(%rbp), %rdx
	movl	-4(%rbp), %eax
	cltq
	movzbl	4(%rdx,%rax), %eax
	cmpb	%al, %cl
	je	.L25
	.loc 1 62 0
	movl	$0, %eax
	jmp	.L22
.L25:
	.loc 1 60 0 discriminator 2
	addl	$1, -4(%rbp)
.L24:
	.loc 1 60 0 is_stmt 0 discriminator 1
	movq	-24(%rbp), %rax
	movl	(%rax), %eax
	cmpl	%eax, -4(%rbp)
	jl	.L26
	.loc 1 63 0 is_stmt 1
	movl	$1, %eax
.L22:
	.loc 1 64 0
	popq	%rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE9:
	.size	stringEqual, .-stringEqual
	.globl	print
	.type	print, @function
print:
.LFB10:
	.loc 1 67 0
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$32, %rsp
	movq	%rdi, -24(%rbp)
	.loc 1 69 0
	movq	-24(%rbp), %rax
	addq	$4, %rax
	movq	%rax, -8(%rbp)
	.loc 1 70 0
	movl	$0, -12(%rbp)
	jmp	.L28
.L29:
	.loc 1 71 0 discriminator 3
	movq	-8(%rbp), %rax
	movzbl	(%rax), %eax
	movzbl	%al, %eax
	movl	%eax, %edi
	call	putchar@PLT
	.loc 1 70 0 discriminator 3
	addl	$1, -12(%rbp)
	addq	$1, -8(%rbp)
.L28:
	.loc 1 70 0 is_stmt 0 discriminator 1
	movq	-24(%rbp), %rax
	movl	(%rax), %eax
	cmpl	%eax, -12(%rbp)
	jl	.L29
	.loc 1 72 0 is_stmt 1
	nop
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE10:
	.size	print, .-print
	.section	.rodata
.LC0:
	.string	"%d"
	.text
	.globl	printi
	.type	printi, @function
printi:
.LFB11:
	.loc 1 75 0
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$16, %rsp
	movl	%edi, -4(%rbp)
	.loc 1 76 0
	movl	-4(%rbp), %eax
	movl	%eax, %esi
	leaq	.LC0(%rip), %rdi
	movl	$0, %eax
	call	printf@PLT
	.loc 1 77 0
	nop
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE11:
	.size	printi, .-printi
	.globl	rttest
	.type	rttest, @function
rttest:
.LFB12:
	.loc 1 79 0
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	.loc 1 80 0
	movl	$116, %edi
	call	putchar@PLT
	.loc 1 81 0
	movl	$101, %edi
	call	putchar@PLT
	.loc 1 82 0
	movl	$115, %edi
	call	putchar@PLT
	.loc 1 83 0
	movl	$116, %edi
	call	putchar@PLT
	.loc 1 84 0
	movq	stdout(%rip), %rax
	movq	%rax, %rdi
	call	fflush@PLT
	.loc 1 85 0
	nop
	popq	%rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE12:
	.size	rttest, .-rttest
	.globl	flush
	.type	flush, @function
flush:
.LFB13:
	.loc 1 88 0
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	.loc 1 89 0
	movq	stdout(%rip), %rax
	movq	%rax, %rdi
	call	fflush@PLT
	.loc 1 90 0
	nop
	popq	%rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE13:
	.size	flush, .-flush
	.comm	consts,2048,32
	.globl	empty
	.bss
	.align 8
	.type	empty, @object
	.size	empty, 8
empty:
	.zero	8
	.text
	.globl	main
	.type	main, @function
main:
.LFB14:
	.loc 1 96 0
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$16, %rsp
	.loc 1 98 0
	movl	$0, -4(%rbp)
	jmp	.L34
.L35:
	.loc 1 100 0 discriminator 3
	movl	-4(%rbp), %eax
	cltq
	leaq	0(,%rax,8), %rdx
	leaq	consts(%rip), %rax
	movl	$1, (%rdx,%rax)
	.loc 1 101 0 discriminator 3
	movl	-4(%rbp), %eax
	movl	%eax, %ecx
	movl	-4(%rbp), %eax
	cltq
	leaq	0(,%rax,8), %rdx
	leaq	4+consts(%rip), %rax
	movb	%cl, (%rdx,%rax)
	.loc 1 98 0 discriminator 3
	addl	$1, -4(%rbp)
.L34:
	.loc 1 98 0 is_stmt 0 discriminator 1
	cmpl	$255, -4(%rbp)
	jle	.L35
	.loc 1 103 0 is_stmt 1
	movl	$0, %edi
	movl	$0, %eax
	call	tigermain@PLT
	.loc 1 104 0
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE14:
	.size	main, .-main
	.globl	ord
	.type	ord, @function
ord:
.LFB15:
	.loc 1 107 0
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	movq	%rdi, -8(%rbp)
	.loc 1 108 0
	movq	-8(%rbp), %rax
	movl	(%rax), %eax
	testl	%eax, %eax
	jne	.L38
	.loc 1 109 0
	movl	$-1, %eax
	jmp	.L39
.L38:
	.loc 1 111 0
	movq	-8(%rbp), %rax
	movzbl	4(%rax), %eax
	movzbl	%al, %eax
.L39:
	.loc 1 112 0
	popq	%rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE15:
	.size	ord, .-ord
	.section	.rodata
.LC1:
	.string	"chr(%d) out of range\n"
	.text
	.globl	chr
	.type	chr, @function
chr:
.LFB16:
	.loc 1 115 0
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$16, %rsp
	movq	%rdi, -8(%rbp)
	.loc 1 116 0
	cmpq	$0, -8(%rbp)
	js	.L41
	.loc 1 116 0 is_stmt 0 discriminator 1
	cmpq	$255, -8(%rbp)
	jle	.L42
.L41:
	.loc 1 118 0 is_stmt 1
	movq	-8(%rbp), %rax
	movq	%rax, %rsi
	leaq	.LC1(%rip), %rdi
	movl	$0, %eax
	call	printf@PLT
	.loc 1 119 0
	movl	$1, %edi
	call	exit@PLT
.L42:
	.loc 1 121 0
	movq	-8(%rbp), %rax
	leaq	0(,%rax,8), %rdx
	leaq	consts(%rip), %rax
	addq	%rdx, %rax
	.loc 1 122 0
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE16:
	.size	chr, .-chr
	.globl	size
	.type	size, @function
size:
.LFB17:
	.loc 1 125 0
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	movq	%rdi, -8(%rbp)
	.loc 1 126 0
	movq	-8(%rbp), %rax
	movl	(%rax), %eax
	.loc 1 127 0
	popq	%rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE17:
	.size	size, .-size
	.section	.rodata
	.align 8
.LC2:
	.string	"substring([%d],%d,%d) out of range\n"
	.text
	.globl	substring
	.type	substring, @function
substring:
.LFB18:
	.loc 1 130 0
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$32, %rsp
	movq	%rdi, -24(%rbp)
	movl	%esi, -28(%rbp)
	movl	%edx, -32(%rbp)
	.loc 1 131 0
	cmpl	$0, -28(%rbp)
	js	.L47
	.loc 1 131 0 is_stmt 0 discriminator 1
	movl	-28(%rbp), %edx
	movl	-32(%rbp), %eax
	addl	%eax, %edx
	movq	-24(%rbp), %rax
	movl	(%rax), %eax
	cmpl	%eax, %edx
	jle	.L48
.L47:
	.loc 1 133 0 is_stmt 1
	movq	-24(%rbp), %rax
	movl	(%rax), %eax
	movl	-32(%rbp), %ecx
	movl	-28(%rbp), %edx
	movl	%eax, %esi
	leaq	.LC2(%rip), %rdi
	movl	$0, %eax
	call	printf@PLT
	.loc 1 134 0
	movl	$1, %edi
	call	exit@PLT
.L48:
	.loc 1 136 0
	cmpl	$1, -32(%rbp)
	jne	.L49
	.loc 1 137 0
	movq	-24(%rbp), %rdx
	movl	-28(%rbp), %eax
	cltq
	movzbl	4(%rdx,%rax), %eax
	movzbl	%al, %eax
	leaq	0(,%rax,8), %rdx
	leaq	consts(%rip), %rax
	addq	%rdx, %rax
	jmp	.L50
.L49:
.LBB2:
	.loc 1 139 0
	movl	-32(%rbp), %eax
	cltq
	addq	$4, %rax
	movq	%rax, %rdi
	call	malloc@PLT
	movq	%rax, -8(%rbp)
	.loc 1 141 0
	movq	-8(%rbp), %rax
	movl	-32(%rbp), %edx
	movl	%edx, (%rax)
	.loc 1 142 0
	movl	$0, -12(%rbp)
	jmp	.L51
.L52:
	.loc 1 143 0 discriminator 3
	movl	-28(%rbp), %edx
	movl	-12(%rbp), %eax
	addl	%edx, %eax
	movq	-24(%rbp), %rdx
	cltq
	movzbl	4(%rdx,%rax), %ecx
	movq	-8(%rbp), %rdx
	movl	-12(%rbp), %eax
	cltq
	movb	%cl, 4(%rdx,%rax)
	.loc 1 142 0 discriminator 3
	addl	$1, -12(%rbp)
.L51:
	.loc 1 142 0 is_stmt 0 discriminator 1
	movl	-12(%rbp), %eax
	cmpl	-32(%rbp), %eax
	jl	.L52
	.loc 1 144 0 is_stmt 1
	movq	-8(%rbp), %rax
.L50:
.LBE2:
	.loc 1 146 0
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE18:
	.size	substring, .-substring
	.globl	concat
	.type	concat, @function
concat:
.LFB19:
	.loc 1 149 0
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$32, %rsp
	movq	%rdi, -24(%rbp)
	movq	%rsi, -32(%rbp)
	.loc 1 150 0
	movq	-24(%rbp), %rax
	movl	(%rax), %eax
	testl	%eax, %eax
	jne	.L54
	.loc 1 151 0
	movq	-32(%rbp), %rax
	jmp	.L55
.L54:
	.loc 1 152 0
	movq	-32(%rbp), %rax
	movl	(%rax), %eax
	testl	%eax, %eax
	jne	.L56
	.loc 1 153 0
	movq	-24(%rbp), %rax
	jmp	.L55
.L56:
.LBB3:
	.loc 1 156 0
	movq	-24(%rbp), %rax
	movl	(%rax), %edx
	movq	-32(%rbp), %rax
	movl	(%rax), %eax
	addl	%edx, %eax
	movl	%eax, -12(%rbp)
	.loc 1 157 0
	movl	-12(%rbp), %eax
	cltq
	addq	$4, %rax
	movq	%rax, %rdi
	call	malloc@PLT
	movq	%rax, -8(%rbp)
	.loc 1 158 0
	movq	-8(%rbp), %rax
	movl	-12(%rbp), %edx
	movl	%edx, (%rax)
	.loc 1 159 0
	movl	$0, -16(%rbp)
	jmp	.L57
.L58:
	.loc 1 160 0 discriminator 3
	movq	-24(%rbp), %rdx
	movl	-16(%rbp), %eax
	cltq
	movzbl	4(%rdx,%rax), %ecx
	movq	-8(%rbp), %rdx
	movl	-16(%rbp), %eax
	cltq
	movb	%cl, 4(%rdx,%rax)
	.loc 1 159 0 discriminator 3
	addl	$1, -16(%rbp)
.L57:
	.loc 1 159 0 is_stmt 0 discriminator 1
	movq	-24(%rbp), %rax
	movl	(%rax), %eax
	cmpl	%eax, -16(%rbp)
	jl	.L58
	.loc 1 161 0 is_stmt 1
	movl	$0, -16(%rbp)
	jmp	.L59
.L60:
	.loc 1 162 0 discriminator 3
	movq	-24(%rbp), %rax
	movl	(%rax), %edx
	movl	-16(%rbp), %eax
	leal	(%rdx,%rax), %esi
	movq	-32(%rbp), %rdx
	movl	-16(%rbp), %eax
	cltq
	movzbl	4(%rdx,%rax), %ecx
	movq	-8(%rbp), %rdx
	movslq	%esi, %rax
	movb	%cl, 4(%rdx,%rax)
	.loc 1 161 0 discriminator 3
	addl	$1, -16(%rbp)
.L59:
	.loc 1 161 0 is_stmt 0 discriminator 1
	movq	-32(%rbp), %rax
	movl	(%rax), %eax
	cmpl	%eax, -16(%rbp)
	jl	.L60
	.loc 1 163 0 is_stmt 1
	movq	-8(%rbp), %rax
.L55:
.LBE3:
	.loc 1 165 0
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE19:
	.size	concat, .-concat
	.globl	not
	.type	not, @function
not:
.LFB20:
	.loc 1 168 0
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	movl	%edi, -4(%rbp)
	.loc 1 169 0
	cmpl	$0, -4(%rbp)
	sete	%al
	movzbl	%al, %eax
	.loc 1 170 0
	popq	%rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE20:
	.size	not, .-not
	.globl	__wrap_getchar
	.type	__wrap_getchar, @function
__wrap_getchar:
.LFB21:
	.loc 1 176 0
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$16, %rsp
	.loc 1 176 0
	movq	stdin(%rip), %rax
	movq	%rax, %rdi
	call	_IO_getc@PLT
	movl	%eax, -4(%rbp)
	.loc 1 177 0
	cmpl	$-1, -4(%rbp)
	jne	.L64
	.loc 1 177 0 is_stmt 0 discriminator 1
	leaq	empty(%rip), %rax
	jmp	.L65
.L64:
	.loc 1 178 0 is_stmt 1
	movl	-4(%rbp), %eax
	cltq
	leaq	0(,%rax,8), %rdx
	leaq	consts(%rip), %rax
	addq	%rdx, %rax
.L65:
	.loc 1 179 0
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE21:
	.size	__wrap_getchar, .-__wrap_getchar
.Letext0:
	.file 2 "/usr/lib/gcc/x86_64-linux-gnu/7/include/stddef.h"
	.file 3 "/usr/include/x86_64-linux-gnu/bits/types.h"
	.file 4 "/usr/include/x86_64-linux-gnu/bits/libio.h"
	.file 5 "/usr/include/stdio.h"
	.file 6 "/usr/include/x86_64-linux-gnu/bits/sys_errlist.h"
	.section	.debug_info,"",@progbits
.Ldebug_info0:
	.long	0x7b4
	.value	0x4
	.long	.Ldebug_abbrev0
	.byte	0x8
	.uleb128 0x1
	.long	.LASF76
	.byte	0xc
	.long	.LASF77
	.long	.LASF78
	.quad	.Ltext0
	.quad	.Letext0-.Ltext0
	.long	.Ldebug_line0
	.uleb128 0x2
	.long	.LASF7
	.byte	0x2
	.byte	0xd8
	.long	0x38
	.uleb128 0x3
	.byte	0x8
	.byte	0x7
	.long	.LASF0
	.uleb128 0x3
	.byte	0x1
	.byte	0x8
	.long	.LASF1
	.uleb128 0x3
	.byte	0x2
	.byte	0x7
	.long	.LASF2
	.uleb128 0x3
	.byte	0x4
	.byte	0x7
	.long	.LASF3
	.uleb128 0x3
	.byte	0x1
	.byte	0x6
	.long	.LASF4
	.uleb128 0x3
	.byte	0x2
	.byte	0x5
	.long	.LASF5
	.uleb128 0x4
	.byte	0x4
	.byte	0x5
	.string	"int"
	.uleb128 0x3
	.byte	0x8
	.byte	0x5
	.long	.LASF6
	.uleb128 0x2
	.long	.LASF8
	.byte	0x3
	.byte	0x8c
	.long	0x69
	.uleb128 0x2
	.long	.LASF9
	.byte	0x3
	.byte	0x8d
	.long	0x69
	.uleb128 0x5
	.byte	0x8
	.uleb128 0x6
	.byte	0x8
	.long	0x8e
	.uleb128 0x3
	.byte	0x1
	.byte	0x6
	.long	.LASF10
	.uleb128 0x7
	.long	0x8e
	.uleb128 0x8
	.long	.LASF40
	.byte	0xd8
	.byte	0x4
	.byte	0xf5
	.long	0x21a
	.uleb128 0x9
	.long	.LASF11
	.byte	0x4
	.byte	0xf6
	.long	0x62
	.byte	0
	.uleb128 0x9
	.long	.LASF12
	.byte	0x4
	.byte	0xfb
	.long	0x88
	.byte	0x8
	.uleb128 0x9
	.long	.LASF13
	.byte	0x4
	.byte	0xfc
	.long	0x88
	.byte	0x10
	.uleb128 0x9
	.long	.LASF14
	.byte	0x4
	.byte	0xfd
	.long	0x88
	.byte	0x18
	.uleb128 0x9
	.long	.LASF15
	.byte	0x4
	.byte	0xfe
	.long	0x88
	.byte	0x20
	.uleb128 0x9
	.long	.LASF16
	.byte	0x4
	.byte	0xff
	.long	0x88
	.byte	0x28
	.uleb128 0xa
	.long	.LASF17
	.byte	0x4
	.value	0x100
	.long	0x88
	.byte	0x30
	.uleb128 0xa
	.long	.LASF18
	.byte	0x4
	.value	0x101
	.long	0x88
	.byte	0x38
	.uleb128 0xa
	.long	.LASF19
	.byte	0x4
	.value	0x102
	.long	0x88
	.byte	0x40
	.uleb128 0xa
	.long	.LASF20
	.byte	0x4
	.value	0x104
	.long	0x88
	.byte	0x48
	.uleb128 0xa
	.long	.LASF21
	.byte	0x4
	.value	0x105
	.long	0x88
	.byte	0x50
	.uleb128 0xa
	.long	.LASF22
	.byte	0x4
	.value	0x106
	.long	0x88
	.byte	0x58
	.uleb128 0xa
	.long	.LASF23
	.byte	0x4
	.value	0x108
	.long	0x252
	.byte	0x60
	.uleb128 0xa
	.long	.LASF24
	.byte	0x4
	.value	0x10a
	.long	0x258
	.byte	0x68
	.uleb128 0xa
	.long	.LASF25
	.byte	0x4
	.value	0x10c
	.long	0x62
	.byte	0x70
	.uleb128 0xa
	.long	.LASF26
	.byte	0x4
	.value	0x110
	.long	0x62
	.byte	0x74
	.uleb128 0xa
	.long	.LASF27
	.byte	0x4
	.value	0x112
	.long	0x70
	.byte	0x78
	.uleb128 0xa
	.long	.LASF28
	.byte	0x4
	.value	0x116
	.long	0x46
	.byte	0x80
	.uleb128 0xa
	.long	.LASF29
	.byte	0x4
	.value	0x117
	.long	0x54
	.byte	0x82
	.uleb128 0xa
	.long	.LASF30
	.byte	0x4
	.value	0x118
	.long	0x25e
	.byte	0x83
	.uleb128 0xa
	.long	.LASF31
	.byte	0x4
	.value	0x11c
	.long	0x26e
	.byte	0x88
	.uleb128 0xa
	.long	.LASF32
	.byte	0x4
	.value	0x125
	.long	0x7b
	.byte	0x90
	.uleb128 0xa
	.long	.LASF33
	.byte	0x4
	.value	0x12d
	.long	0x86
	.byte	0x98
	.uleb128 0xa
	.long	.LASF34
	.byte	0x4
	.value	0x12e
	.long	0x86
	.byte	0xa0
	.uleb128 0xa
	.long	.LASF35
	.byte	0x4
	.value	0x12f
	.long	0x86
	.byte	0xa8
	.uleb128 0xa
	.long	.LASF36
	.byte	0x4
	.value	0x130
	.long	0x86
	.byte	0xb0
	.uleb128 0xa
	.long	.LASF37
	.byte	0x4
	.value	0x132
	.long	0x2d
	.byte	0xb8
	.uleb128 0xa
	.long	.LASF38
	.byte	0x4
	.value	0x133
	.long	0x62
	.byte	0xc0
	.uleb128 0xa
	.long	.LASF39
	.byte	0x4
	.value	0x135
	.long	0x274
	.byte	0xc4
	.byte	0
	.uleb128 0xb
	.long	.LASF79
	.byte	0x4
	.byte	0x9a
	.uleb128 0x8
	.long	.LASF41
	.byte	0x18
	.byte	0x4
	.byte	0xa0
	.long	0x252
	.uleb128 0x9
	.long	.LASF42
	.byte	0x4
	.byte	0xa1
	.long	0x252
	.byte	0
	.uleb128 0x9
	.long	.LASF43
	.byte	0x4
	.byte	0xa2
	.long	0x258
	.byte	0x8
	.uleb128 0x9
	.long	.LASF44
	.byte	0x4
	.byte	0xa6
	.long	0x62
	.byte	0x10
	.byte	0
	.uleb128 0x6
	.byte	0x8
	.long	0x221
	.uleb128 0x6
	.byte	0x8
	.long	0x9a
	.uleb128 0xc
	.long	0x8e
	.long	0x26e
	.uleb128 0xd
	.long	0x38
	.byte	0
	.byte	0
	.uleb128 0x6
	.byte	0x8
	.long	0x21a
	.uleb128 0xc
	.long	0x8e
	.long	0x284
	.uleb128 0xd
	.long	0x38
	.byte	0x13
	.byte	0
	.uleb128 0xe
	.long	.LASF80
	.uleb128 0xf
	.long	.LASF45
	.byte	0x4
	.value	0x13f
	.long	0x284
	.uleb128 0xf
	.long	.LASF46
	.byte	0x4
	.value	0x140
	.long	0x284
	.uleb128 0xf
	.long	.LASF47
	.byte	0x4
	.value	0x141
	.long	0x284
	.uleb128 0x6
	.byte	0x8
	.long	0x95
	.uleb128 0x7
	.long	0x2ad
	.uleb128 0x10
	.long	.LASF48
	.byte	0x5
	.byte	0x87
	.long	0x258
	.uleb128 0x10
	.long	.LASF49
	.byte	0x5
	.byte	0x88
	.long	0x258
	.uleb128 0x10
	.long	.LASF50
	.byte	0x5
	.byte	0x89
	.long	0x258
	.uleb128 0x10
	.long	.LASF51
	.byte	0x6
	.byte	0x1a
	.long	0x62
	.uleb128 0xc
	.long	0x2b3
	.long	0x2ef
	.uleb128 0x11
	.byte	0
	.uleb128 0x7
	.long	0x2e4
	.uleb128 0x10
	.long	.LASF52
	.byte	0x6
	.byte	0x1b
	.long	0x2ef
	.uleb128 0x3
	.byte	0x8
	.byte	0x5
	.long	.LASF53
	.uleb128 0x3
	.byte	0x8
	.byte	0x7
	.long	.LASF54
	.uleb128 0x8
	.long	.LASF55
	.byte	0x8
	.byte	0x1
	.byte	0x22
	.long	0x332
	.uleb128 0x9
	.long	.LASF56
	.byte	0x1
	.byte	0x24
	.long	0x62
	.byte	0
	.uleb128 0x9
	.long	.LASF57
	.byte	0x1
	.byte	0x25
	.long	0x332
	.byte	0x4
	.byte	0
	.uleb128 0xc
	.long	0x3f
	.long	0x342
	.uleb128 0xd
	.long	0x38
	.byte	0
	.byte	0
	.uleb128 0xc
	.long	0x30d
	.long	0x352
	.uleb128 0xd
	.long	0x38
	.byte	0xff
	.byte	0
	.uleb128 0x12
	.long	.LASF58
	.byte	0x1
	.byte	0x5c
	.long	0x342
	.uleb128 0x9
	.byte	0x3
	.quad	consts
	.uleb128 0x12
	.long	.LASF59
	.byte	0x1
	.byte	0x5d
	.long	0x30d
	.uleb128 0x9
	.byte	0x3
	.quad	empty
	.uleb128 0x13
	.long	.LASF64
	.byte	0x1
	.byte	0xaf
	.long	0x3aa
	.quad	.LFB21
	.quad	.LFE21-.LFB21
	.uleb128 0x1
	.byte	0x9c
	.long	0x3aa
	.uleb128 0x14
	.string	"i"
	.byte	0x1
	.byte	0xb0
	.long	0x62
	.uleb128 0x2
	.byte	0x91
	.sleb128 -20
	.byte	0
	.uleb128 0x6
	.byte	0x8
	.long	0x30d
	.uleb128 0x15
	.string	"not"
	.byte	0x1
	.byte	0xa7
	.long	0x62
	.quad	.LFB20
	.quad	.LFE20-.LFB20
	.uleb128 0x1
	.byte	0x9c
	.long	0x3de
	.uleb128 0x16
	.string	"i"
	.byte	0x1
	.byte	0xa7
	.long	0x62
	.uleb128 0x2
	.byte	0x91
	.sleb128 -20
	.byte	0
	.uleb128 0x17
	.long	.LASF60
	.byte	0x1
	.byte	0x94
	.long	0x3aa
	.quad	.LFB19
	.quad	.LFE19-.LFB19
	.uleb128 0x1
	.byte	0x9c
	.long	0x44e
	.uleb128 0x16
	.string	"a"
	.byte	0x1
	.byte	0x94
	.long	0x3aa
	.uleb128 0x2
	.byte	0x91
	.sleb128 -40
	.uleb128 0x16
	.string	"b"
	.byte	0x1
	.byte	0x94
	.long	0x3aa
	.uleb128 0x2
	.byte	0x91
	.sleb128 -48
	.uleb128 0x18
	.quad	.LBB3
	.quad	.LBE3-.LBB3
	.uleb128 0x14
	.string	"i"
	.byte	0x1
	.byte	0x9c
	.long	0x62
	.uleb128 0x2
	.byte	0x91
	.sleb128 -32
	.uleb128 0x14
	.string	"n"
	.byte	0x1
	.byte	0x9c
	.long	0x62
	.uleb128 0x2
	.byte	0x91
	.sleb128 -28
	.uleb128 0x14
	.string	"t"
	.byte	0x1
	.byte	0x9d
	.long	0x3aa
	.uleb128 0x2
	.byte	0x91
	.sleb128 -24
	.byte	0
	.byte	0
	.uleb128 0x17
	.long	.LASF61
	.byte	0x1
	.byte	0x81
	.long	0x3aa
	.quad	.LFB18
	.quad	.LFE18-.LFB18
	.uleb128 0x1
	.byte	0x9c
	.long	0x4c0
	.uleb128 0x16
	.string	"s"
	.byte	0x1
	.byte	0x81
	.long	0x3aa
	.uleb128 0x2
	.byte	0x91
	.sleb128 -40
	.uleb128 0x19
	.long	.LASF62
	.byte	0x1
	.byte	0x81
	.long	0x62
	.uleb128 0x2
	.byte	0x91
	.sleb128 -44
	.uleb128 0x16
	.string	"n"
	.byte	0x1
	.byte	0x81
	.long	0x62
	.uleb128 0x2
	.byte	0x91
	.sleb128 -48
	.uleb128 0x18
	.quad	.LBB2
	.quad	.LBE2-.LBB2
	.uleb128 0x14
	.string	"t"
	.byte	0x1
	.byte	0x8b
	.long	0x3aa
	.uleb128 0x2
	.byte	0x91
	.sleb128 -24
	.uleb128 0x14
	.string	"i"
	.byte	0x1
	.byte	0x8c
	.long	0x62
	.uleb128 0x2
	.byte	0x91
	.sleb128 -28
	.byte	0
	.byte	0
	.uleb128 0x1a
	.long	.LASF63
	.byte	0x1
	.byte	0x7c
	.long	0x62
	.quad	.LFB17
	.quad	.LFE17-.LFB17
	.uleb128 0x1
	.byte	0x9c
	.long	0x4ee
	.uleb128 0x16
	.string	"s"
	.byte	0x1
	.byte	0x7c
	.long	0x3aa
	.uleb128 0x2
	.byte	0x91
	.sleb128 -24
	.byte	0
	.uleb128 0x1b
	.string	"chr"
	.byte	0x1
	.byte	0x72
	.long	0x3aa
	.quad	.LFB16
	.quad	.LFE16-.LFB16
	.uleb128 0x1
	.byte	0x9c
	.long	0x51c
	.uleb128 0x16
	.string	"i"
	.byte	0x1
	.byte	0x72
	.long	0x69
	.uleb128 0x2
	.byte	0x91
	.sleb128 -24
	.byte	0
	.uleb128 0x15
	.string	"ord"
	.byte	0x1
	.byte	0x6a
	.long	0x62
	.quad	.LFB15
	.quad	.LFE15-.LFB15
	.uleb128 0x1
	.byte	0x9c
	.long	0x54a
	.uleb128 0x16
	.string	"s"
	.byte	0x1
	.byte	0x6a
	.long	0x3aa
	.uleb128 0x2
	.byte	0x91
	.sleb128 -24
	.byte	0
	.uleb128 0x13
	.long	.LASF65
	.byte	0x1
	.byte	0x5f
	.long	0x62
	.quad	.LFB14
	.quad	.LFE14-.LFB14
	.uleb128 0x1
	.byte	0x9c
	.long	0x585
	.uleb128 0x14
	.string	"i"
	.byte	0x1
	.byte	0x61
	.long	0x62
	.uleb128 0x2
	.byte	0x91
	.sleb128 -20
	.uleb128 0x1c
	.long	.LASF81
	.byte	0x1
	.byte	0x67
	.long	0x62
	.uleb128 0x1d
	.byte	0
	.byte	0
	.uleb128 0x1e
	.long	.LASF66
	.byte	0x1
	.byte	0x57
	.quad	.LFB13
	.quad	.LFE13-.LFB13
	.uleb128 0x1
	.byte	0x9c
	.uleb128 0x1e
	.long	.LASF67
	.byte	0x1
	.byte	0x4f
	.quad	.LFB12
	.quad	.LFE12-.LFB12
	.uleb128 0x1
	.byte	0x9c
	.uleb128 0x1f
	.long	.LASF68
	.byte	0x1
	.byte	0x4a
	.quad	.LFB11
	.quad	.LFE11-.LFB11
	.uleb128 0x1
	.byte	0x9c
	.long	0x5e1
	.uleb128 0x16
	.string	"n"
	.byte	0x1
	.byte	0x4a
	.long	0x62
	.uleb128 0x2
	.byte	0x91
	.sleb128 -20
	.byte	0
	.uleb128 0x1f
	.long	.LASF69
	.byte	0x1
	.byte	0x42
	.quad	.LFB10
	.quad	.LFE10-.LFB10
	.uleb128 0x1
	.byte	0x9c
	.long	0x623
	.uleb128 0x16
	.string	"s"
	.byte	0x1
	.byte	0x42
	.long	0x3aa
	.uleb128 0x2
	.byte	0x91
	.sleb128 -40
	.uleb128 0x14
	.string	"i"
	.byte	0x1
	.byte	0x44
	.long	0x62
	.uleb128 0x2
	.byte	0x91
	.sleb128 -28
	.uleb128 0x14
	.string	"p"
	.byte	0x1
	.byte	0x45
	.long	0x623
	.uleb128 0x2
	.byte	0x91
	.sleb128 -24
	.byte	0
	.uleb128 0x6
	.byte	0x8
	.long	0x3f
	.uleb128 0x1a
	.long	.LASF70
	.byte	0x1
	.byte	0x35
	.long	0x62
	.quad	.LFB9
	.quad	.LFE9-.LFB9
	.uleb128 0x1
	.byte	0x9c
	.long	0x66f
	.uleb128 0x16
	.string	"s"
	.byte	0x1
	.byte	0x35
	.long	0x3aa
	.uleb128 0x2
	.byte	0x91
	.sleb128 -40
	.uleb128 0x16
	.string	"t"
	.byte	0x1
	.byte	0x35
	.long	0x3aa
	.uleb128 0x2
	.byte	0x91
	.sleb128 -48
	.uleb128 0x14
	.string	"i"
	.byte	0x1
	.byte	0x37
	.long	0x62
	.uleb128 0x2
	.byte	0x91
	.sleb128 -20
	.byte	0
	.uleb128 0x1a
	.long	.LASF71
	.byte	0x1
	.byte	0x28
	.long	0x62
	.quad	.LFB8
	.quad	.LFE8-.LFB8
	.uleb128 0x1
	.byte	0x9c
	.long	0x6b5
	.uleb128 0x16
	.string	"s"
	.byte	0x1
	.byte	0x28
	.long	0x3aa
	.uleb128 0x2
	.byte	0x91
	.sleb128 -40
	.uleb128 0x16
	.string	"t"
	.byte	0x1
	.byte	0x28
	.long	0x3aa
	.uleb128 0x2
	.byte	0x91
	.sleb128 -48
	.uleb128 0x14
	.string	"i"
	.byte	0x1
	.byte	0x2a
	.long	0x62
	.uleb128 0x2
	.byte	0x91
	.sleb128 -20
	.byte	0
	.uleb128 0x17
	.long	.LASF72
	.byte	0x1
	.byte	0x18
	.long	0x709
	.quad	.LFB7
	.quad	.LFE7-.LFB7
	.uleb128 0x1
	.byte	0x9c
	.long	0x709
	.uleb128 0x19
	.long	.LASF63
	.byte	0x1
	.byte	0x18
	.long	0x62
	.uleb128 0x2
	.byte	0x91
	.sleb128 -52
	.uleb128 0x14
	.string	"i"
	.byte	0x1
	.byte	0x1a
	.long	0x62
	.uleb128 0x2
	.byte	0x91
	.sleb128 -36
	.uleb128 0x14
	.string	"p"
	.byte	0x1
	.byte	0x1b
	.long	0x709
	.uleb128 0x2
	.byte	0x91
	.sleb128 -32
	.uleb128 0x14
	.string	"a"
	.byte	0x1
	.byte	0x1b
	.long	0x709
	.uleb128 0x2
	.byte	0x91
	.sleb128 -24
	.byte	0
	.uleb128 0x6
	.byte	0x8
	.long	0x69
	.uleb128 0x17
	.long	.LASF73
	.byte	0x1
	.byte	0xd
	.long	0x709
	.quad	.LFB6
	.quad	.LFE6-.LFB6
	.uleb128 0x1
	.byte	0x9c
	.long	0x765
	.uleb128 0x19
	.long	.LASF63
	.byte	0x1
	.byte	0xd
	.long	0x62
	.uleb128 0x2
	.byte	0x91
	.sleb128 -36
	.uleb128 0x19
	.long	.LASF74
	.byte	0x1
	.byte	0xd
	.long	0x62
	.uleb128 0x2
	.byte	0x91
	.sleb128 -40
	.uleb128 0x14
	.string	"i"
	.byte	0x1
	.byte	0xf
	.long	0x62
	.uleb128 0x2
	.byte	0x91
	.sleb128 -28
	.uleb128 0x14
	.string	"a"
	.byte	0x1
	.byte	0x10
	.long	0x709
	.uleb128 0x2
	.byte	0x91
	.sleb128 -24
	.byte	0
	.uleb128 0x20
	.long	.LASF75
	.byte	0x1
	.byte	0x4
	.long	0x709
	.quad	.LFB5
	.quad	.LFE5-.LFB5
	.uleb128 0x1
	.byte	0x9c
	.uleb128 0x19
	.long	.LASF63
	.byte	0x1
	.byte	0x4
	.long	0x62
	.uleb128 0x2
	.byte	0x91
	.sleb128 -36
	.uleb128 0x19
	.long	.LASF74
	.byte	0x1
	.byte	0x4
	.long	0x62
	.uleb128 0x2
	.byte	0x91
	.sleb128 -40
	.uleb128 0x14
	.string	"i"
	.byte	0x1
	.byte	0x6
	.long	0x62
	.uleb128 0x2
	.byte	0x91
	.sleb128 -28
	.uleb128 0x14
	.string	"a"
	.byte	0x1
	.byte	0x7
	.long	0x709
	.uleb128 0x2
	.byte	0x91
	.sleb128 -24
	.byte	0
	.byte	0
	.section	.debug_abbrev,"",@progbits
.Ldebug_abbrev0:
	.uleb128 0x1
	.uleb128 0x11
	.byte	0x1
	.uleb128 0x25
	.uleb128 0xe
	.uleb128 0x13
	.uleb128 0xb
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x1b
	.uleb128 0xe
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x10
	.uleb128 0x17
	.byte	0
	.byte	0
	.uleb128 0x2
	.uleb128 0x16
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x3
	.uleb128 0x24
	.byte	0
	.uleb128 0xb
	.uleb128 0xb
	.uleb128 0x3e
	.uleb128 0xb
	.uleb128 0x3
	.uleb128 0xe
	.byte	0
	.byte	0
	.uleb128 0x4
	.uleb128 0x24
	.byte	0
	.uleb128 0xb
	.uleb128 0xb
	.uleb128 0x3e
	.uleb128 0xb
	.uleb128 0x3
	.uleb128 0x8
	.byte	0
	.byte	0
	.uleb128 0x5
	.uleb128 0xf
	.byte	0
	.uleb128 0xb
	.uleb128 0xb
	.byte	0
	.byte	0
	.uleb128 0x6
	.uleb128 0xf
	.byte	0
	.uleb128 0xb
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x7
	.uleb128 0x26
	.byte	0
	.uleb128 0x49
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x8
	.uleb128 0x13
	.byte	0x1
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0xb
	.uleb128 0xb
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x9
	.uleb128 0xd
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x38
	.uleb128 0xb
	.byte	0
	.byte	0
	.uleb128 0xa
	.uleb128 0xd
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0x5
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x38
	.uleb128 0xb
	.byte	0
	.byte	0
	.uleb128 0xb
	.uleb128 0x16
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.byte	0
	.byte	0
	.uleb128 0xc
	.uleb128 0x1
	.byte	0x1
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0xd
	.uleb128 0x21
	.byte	0
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x2f
	.uleb128 0xb
	.byte	0
	.byte	0
	.uleb128 0xe
	.uleb128 0x13
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3c
	.uleb128 0x19
	.byte	0
	.byte	0
	.uleb128 0xf
	.uleb128 0x34
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0x5
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3c
	.uleb128 0x19
	.byte	0
	.byte	0
	.uleb128 0x10
	.uleb128 0x34
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3c
	.uleb128 0x19
	.byte	0
	.byte	0
	.uleb128 0x11
	.uleb128 0x21
	.byte	0
	.byte	0
	.byte	0
	.uleb128 0x12
	.uleb128 0x34
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x2
	.uleb128 0x18
	.byte	0
	.byte	0
	.uleb128 0x13
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x40
	.uleb128 0x18
	.uleb128 0x2116
	.uleb128 0x19
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x14
	.uleb128 0x34
	.byte	0
	.uleb128 0x3
	.uleb128 0x8
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x2
	.uleb128 0x18
	.byte	0
	.byte	0
	.uleb128 0x15
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0x8
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x27
	.uleb128 0x19
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x40
	.uleb128 0x18
	.uleb128 0x2117
	.uleb128 0x19
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x16
	.uleb128 0x5
	.byte	0
	.uleb128 0x3
	.uleb128 0x8
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x2
	.uleb128 0x18
	.byte	0
	.byte	0
	.uleb128 0x17
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x27
	.uleb128 0x19
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x40
	.uleb128 0x18
	.uleb128 0x2116
	.uleb128 0x19
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x18
	.uleb128 0xb
	.byte	0x1
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.byte	0
	.byte	0
	.uleb128 0x19
	.uleb128 0x5
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x2
	.uleb128 0x18
	.byte	0
	.byte	0
	.uleb128 0x1a
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x27
	.uleb128 0x19
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x40
	.uleb128 0x18
	.uleb128 0x2117
	.uleb128 0x19
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x1b
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0x8
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x27
	.uleb128 0x19
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x40
	.uleb128 0x18
	.uleb128 0x2116
	.uleb128 0x19
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x1c
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x3c
	.uleb128 0x19
	.byte	0
	.byte	0
	.uleb128 0x1d
	.uleb128 0x18
	.byte	0
	.byte	0
	.byte	0
	.uleb128 0x1e
	.uleb128 0x2e
	.byte	0
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x40
	.uleb128 0x18
	.uleb128 0x2116
	.uleb128 0x19
	.byte	0
	.byte	0
	.uleb128 0x1f
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x27
	.uleb128 0x19
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x40
	.uleb128 0x18
	.uleb128 0x2116
	.uleb128 0x19
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x20
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x27
	.uleb128 0x19
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x40
	.uleb128 0x18
	.uleb128 0x2116
	.uleb128 0x19
	.byte	0
	.byte	0
	.byte	0
	.section	.debug_aranges,"",@progbits
	.long	0x2c
	.value	0x2
	.long	.Ldebug_info0
	.byte	0x8
	.byte	0
	.value	0
	.value	0
	.quad	.Ltext0
	.quad	.Letext0-.Ltext0
	.quad	0
	.quad	0
	.section	.debug_line,"",@progbits
.Ldebug_line0:
	.section	.debug_str,"MS",@progbits,1
.LASF8:
	.string	"__off_t"
.LASF12:
	.string	"_IO_read_ptr"
.LASF24:
	.string	"_chain"
.LASF69:
	.string	"print"
.LASF7:
	.string	"size_t"
.LASF30:
	.string	"_shortbuf"
.LASF55:
	.string	"string"
.LASF76:
	.string	"GNU C11 7.5.0 -mtune=generic -march=x86-64 -g -fstack-protector-strong"
.LASF74:
	.string	"init"
.LASF47:
	.string	"_IO_2_1_stderr_"
.LASF18:
	.string	"_IO_buf_base"
.LASF54:
	.string	"long long unsigned int"
.LASF68:
	.string	"printi"
.LASF75:
	.string	"initArray"
.LASF67:
	.string	"rttest"
.LASF53:
	.string	"long long int"
.LASF4:
	.string	"signed char"
.LASF72:
	.string	"initRecord"
.LASF71:
	.string	"streq"
.LASF25:
	.string	"_fileno"
.LASF13:
	.string	"_IO_read_end"
.LASF59:
	.string	"empty"
.LASF6:
	.string	"long int"
.LASF11:
	.string	"_flags"
.LASF78:
	.string	"/home/david/Workspace/Personal/tigerlang/compiler/src/main/resources/runtimes"
.LASF28:
	.string	"_cur_column"
.LASF27:
	.string	"_old_offset"
.LASF32:
	.string	"_offset"
.LASF60:
	.string	"concat"
.LASF41:
	.string	"_IO_marker"
.LASF48:
	.string	"stdin"
.LASF3:
	.string	"unsigned int"
.LASF81:
	.string	"tigermain"
.LASF0:
	.string	"long unsigned int"
.LASF80:
	.string	"_IO_FILE_plus"
.LASF16:
	.string	"_IO_write_ptr"
.LASF51:
	.string	"sys_nerr"
.LASF43:
	.string	"_sbuf"
.LASF63:
	.string	"size"
.LASF2:
	.string	"short unsigned int"
.LASF20:
	.string	"_IO_save_base"
.LASF61:
	.string	"substring"
.LASF31:
	.string	"_lock"
.LASF26:
	.string	"_flags2"
.LASF38:
	.string	"_mode"
.LASF49:
	.string	"stdout"
.LASF45:
	.string	"_IO_2_1_stdin_"
.LASF73:
	.string	"initArrayBoundsChecked"
.LASF17:
	.string	"_IO_write_end"
.LASF79:
	.string	"_IO_lock_t"
.LASF40:
	.string	"_IO_FILE"
.LASF44:
	.string	"_pos"
.LASF52:
	.string	"sys_errlist"
.LASF23:
	.string	"_markers"
.LASF1:
	.string	"unsigned char"
.LASF19:
	.string	"_IO_buf_end"
.LASF5:
	.string	"short int"
.LASF29:
	.string	"_vtable_offset"
.LASF46:
	.string	"_IO_2_1_stdout_"
.LASF70:
	.string	"stringEqual"
.LASF62:
	.string	"first"
.LASF56:
	.string	"length"
.LASF10:
	.string	"char"
.LASF64:
	.string	"__wrap_getchar"
.LASF77:
	.string	"runtime.c"
.LASF42:
	.string	"_next"
.LASF9:
	.string	"__off64_t"
.LASF14:
	.string	"_IO_read_base"
.LASF22:
	.string	"_IO_save_end"
.LASF33:
	.string	"__pad1"
.LASF34:
	.string	"__pad2"
.LASF35:
	.string	"__pad3"
.LASF36:
	.string	"__pad4"
.LASF37:
	.string	"__pad5"
.LASF66:
	.string	"flush"
.LASF39:
	.string	"_unused2"
.LASF50:
	.string	"stderr"
.LASF58:
	.string	"consts"
.LASF21:
	.string	"_IO_backup_base"
.LASF57:
	.string	"chars"
.LASF65:
	.string	"main"
.LASF15:
	.string	"_IO_write_base"
	.ident	"GCC: (Ubuntu 7.5.0-3ubuntu1~18.04) 7.5.0"
	.section	.note.GNU-stack,"",@progbits
