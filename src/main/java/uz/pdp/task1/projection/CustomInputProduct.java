package uz.pdp.task1.projection;

import org.springframework.data.rest.core.config.Projection;
import uz.pdp.task1.constants.ExpireStatus;
import uz.pdp.task1.entity.Input;
import uz.pdp.task1.entity.InputProduct;
import uz.pdp.task1.entity.Product;

import java.util.Date;

@Projection(types = InputProduct.class)
public interface CustomInputProduct {
    Integer getId();

    Product getProduct();

    Double getAmount();

    Double getPrice();

    Date getExpireDate();

    Input getInput();

    ExpireStatus getExpireStatus();
}
