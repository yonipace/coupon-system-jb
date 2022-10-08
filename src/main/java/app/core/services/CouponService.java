package app.core.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.core.entities.Coupon;
import app.core.exceptions.CouponSystemException;
import app.core.repositories.CouponRepository;

@Service
@Transactional
public class CouponService {

	@Autowired
	CouponRepository couponRepository;

	public List<Coupon> getAllCoupons() {

		return couponRepository.findAll();

	}

	public Coupon getOneCoupon(int id) throws CouponSystemException {

		return couponRepository.findById(id).orElseThrow(() -> new CouponSystemException("Failed to get coupon " + id));

	}

}
