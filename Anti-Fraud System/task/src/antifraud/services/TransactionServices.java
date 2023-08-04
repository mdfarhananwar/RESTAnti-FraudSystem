package antifraud.services;

import antifraud.models.transactionModel.Transaction;
import org.springframework.stereotype.Service;

@Service
public class TransactionServices {

   public boolean validTransaction(Transaction transaction) {
      if (transaction.getAmount() > 0 || transaction.getAmount() != null) {
         return true;
      } else {
         return false;
      }

   }


}
