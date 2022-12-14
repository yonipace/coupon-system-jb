package app.core.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.entities.Customer;
import app.core.exceptions.CouponSystemException;

@Service
@Transactional
public class CustomerService extends ClientService {

	@Override
	public Customer login(String email, String password) throws CouponSystemException {

		return this.customerRepo.findByEmailAndPassword(email, password)
				.orElseThrow(() -> new CouponSystemException("Failed to login, email or password are incorrect"));

	}

	public Coupon purchaseCoupon(Coupon coupon, int id) throws CouponSystemException {

		// check if coupon exists
		if (!couponRepo.existsById(coupon.getId())) {
			throw new CouponSystemException("failed to purchase coupon " + coupon.getId() + " - coupon does not exist");
		}
		// check if coupon has been purchased by the customer
		if (couponRepo.existsByIdAndCustomersId(coupon.getId(), id)) {
			throw new CouponSystemException(
					"failed to purchase coupon " + coupon.getId() + " - coupon already purchased");
		}
		// check if the coupon is out of stock
		if (coupon.getAmount() == 0) {
			throw new CouponSystemException(
					"failed to purchase coupon " + coupon.getId() + " - coupon is out of stock");
		}
		// check if the coupon has expired
		if (coupon.getEndDate().isBefore(LocalDate.now())) {
			throw new CouponSystemException("failed to purchase coupon " + coupon.getId() + " - coupon expired");

		}
		// purchase coupon
		getCustomerDetails(id).addCoupon(coupon);
		// update coupon amount
		coupon.setAmount(coupon.getAmount() - 1);
		// update coupon in DB
		return couponRepo.save(coupon);

//		System.out.printf("coupon %d purchased successfully \n", coupon.getId());
	}

	public List<Coupon> getCustomerCoupons(int id) throws CouponSystemException {

		return couponRepo.findAllByCustomersId(id);

	}

	public List<Coupon> getCustomerCoupons(Category category, int id) throws CouponSystemException {

		return couponRepo.findAllByCustomersIdAndCategory(id, category);

	}

	public List<Coupon> getCustomerCoupons(double maxPrice, int id) throws CouponSystemException {

		return couponRepo.findAllByCustomersIdAndPriceLessThanEqual(id, maxPrice);

	}

	public Customer getCustomerDetails(int id) throws CouponSystemException {

		return customerRepo.findById(id).orElseThrow(

				() -> new CouponSystemException("failed to get customer " + id));

	}

}
