package app.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.entities.Coupon;
import app.core.exceptions.CouponSystemException;
import app.core.services.CouponService;

@RestController
@CrossOrigin
@RequestMapping("/coupons")
public class CouponController {

	@Autowired
	CouponService couponService;

	@GetMapping
	public List<Coupon> getAllCoupons() {

		System.out.println(couponService.getAllCoupons());

		return couponService.getAllCoupons();

	}

	@GetMapping("/get-one")
	public Coupon getOneCoupon(@RequestParam int id) {

		try {
			return couponService.getOneCoupon(id);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}

	}

}
