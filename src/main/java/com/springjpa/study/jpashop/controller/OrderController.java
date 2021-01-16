package com.springjpa.study.jpashop.controller;

import com.springjpa.study.jpashop.domain.Member;
import com.springjpa.study.jpashop.domain.Order;
import com.springjpa.study.jpashop.domain.item.Item;
import com.springjpa.study.jpashop.repo.OrderSearch;
import com.springjpa.study.jpashop.service.ItemService;
import com.springjpa.study.jpashop.service.MemberService;
import com.springjpa.study.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;


    @GetMapping("/order")
    public String createForm(Model model) {
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();
        OrderForm orderForm = new OrderForm(members, items);
        model.addAttribute("orderForm", orderForm);
        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@Valid @ModelAttribute("form") OrderForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "order/orderForm";
        }
        orderService.order(form.getMemberId(), form.getItemId(), form.getCount());
        return "redirect:/";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);
        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}