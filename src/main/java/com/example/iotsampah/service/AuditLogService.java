package com.example.iotsampah.service;

import com.example.iotsampah.entity.MstAudits;
import com.example.iotsampah.entity.MstItems;
import com.example.iotsampah.entity.MstUsers;
import com.example.iotsampah.repository.MstAuditsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    @Autowired
    MstAuditsRepository mstAuditsRepository;

    @Autowired
    MstItemsService mstItemsService;

    @Autowired
    WebClientService webClientService;

    public void auditLogPoint(Integer point, MstUsers mstUsers) {
        MstItems mstItems = mstItemsService.getItemByCode("BTL");

        MstAudits mstAudits = new MstAudits();
        mstAudits.setType("LOG");
        mstAudits.setPoint(point);
        mstAudits.setSaldo(point * mstItems.getPrice()); // saldo
        mstAudits.setCreatedBy(mstUsers);
        mstAuditsRepository.save(mstAudits);
    }

    public boolean auditUpdatePoint(Integer newPoint, MstUsers mstUsers) {
        MstItems mstItems = mstItemsService.getItemByCode("BTL");

        MstAudits mstAudits = new MstAudits();
        mstAudits.setType("MUTATION");
        mstAudits.setPoint(newPoint);

        // current saldo =
        int currSaldo = mstUsers.getSaldo();
        int newSaldo = newPoint * mstItems.getPrice();
        mstAudits.setSaldo(currSaldo + newSaldo); // saldo
        mstAudits.setCreatedBy(mstUsers);
//        boolean isUpdated = true;
        boolean isUpdated = webClientService.updateBalanceStudent(mstUsers.getSchool().getUrl(), mstUsers.getStudentId(), newSaldo);
        if (isUpdated) {
            mstAuditsRepository.save(mstAudits);
        }
        return isUpdated;
    }
}
