package io.github.wcnnkh.jdk.doubt;

import java.lang.reflect.Modifier;

public class ModifierBug {
	public static void main(String[] args) {
		// 为什么int类型会是抽象的
		System.out.println(Modifier.isAbstract(int.class.getModifiers()));
	}
}
