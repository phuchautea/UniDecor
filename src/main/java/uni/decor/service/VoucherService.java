package uni.decor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.decor.entity.Voucher;
import uni.decor.repository.IVoucherRepository;

import java.util.List;

@Service
public class VoucherService {
    @Autowired
    private IVoucherRepository voucherRepository;

    public List<Voucher> getAllVouchers()
    {
        return voucherRepository.findAll();
    }
    public Voucher getVoucherById(Long id)
    {
        return voucherRepository.findById(id).orElse(null);
    }
    public Voucher save(Voucher voucher)
    {
        return voucherRepository.save(voucher);
    }

    public void addVoucher(Voucher voucher)
    {
        save(voucher);
    }
    public void deleteVoucher(Long id)
    {
        voucherRepository.deleteById(id);
    }
    public void updateVoucher(Voucher voucher)
    {
        save(voucher);
    }
}

