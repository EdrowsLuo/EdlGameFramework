package com.edlplan.framework.game.g2d;

/**
 *
 * 关于一些设计：
 *
 * GameObject初始化时通过Game2D.getGlobalObjectByTag/ID获取到各种Engine
 * 比如要添加绘制的就在获取到GraphicEngine后通过GraphicEngine创造连接到具体的绘制对象
 * 要处理触摸事件的就在获取到InputEngine后添加Input监听器来实现。
 *
 * 所有的操作对象都通过各种Engine来代理生成，
 * 在Engine的实现里应该普遍使用链表来实现在Destroy一个GameObject时可以快速的清除相关Engine的引用
 *
 * 一个好的Engine连接应该是单向的，以保证在没有GameObjects（GameObject被Destroy的瞬间）时不会因为找不到
 * 对应的GameObject或者对应的GameObject已经被销毁而产生无效的逻辑。
 * 如果确实需要，则或许应该添加一个判断机制来处理这种情况
 *
 *
 *               Game
 *           全局的对象管理
 *
 *
 *             GameObject                     Engine
 *          游戏对象的数据管理           将各种具体的游戏对象数据
 *                                   进行具体操作处理，比如绘制，物理
 *
 *
 *             GameBehavior
 *          对游戏对象数据的操作
 *
 *            Behavior.Update
 *       一般而言定义为伴随GameObject
 *         整个生命周期的Behavior
 *
 *          Behavior.Controllable
 *    将可以进行的具体操作暴露出来的Behavior
 *   调用这些具体操作的Behavior一般称为Controller
 *
 *          Behavior.Controller
 *   一般指那些对对象进行具体操作的Behavior
 *        比如移动对象，改变对象状态
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
public class Ideas {
}

