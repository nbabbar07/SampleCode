# SampleCode

Create a RESTful web service that stores financial transactions in-memory database and
returns information about
those transactions. It is ok that all data is gone with a shutdown/restart of the application.
The service should be written in Java, and Springboot be started as easily.
The transactions to be stored have a transaction_id, a type, a currency, and an amount.
The service should support returning
• All transactions of a type
• All transactions of a currency.
• Also, transactions can be linked to each other (using a "parent_id") and the total amount
involved
for all transactions linked to a particular transaction can also be queried.
In general, we are looking for a good implementation, code quality, and how the
implementation is tested.
Do not put the code in a publicly available repository!
API Spec:
PUT /bookingservice/transaction/$transaction_id
Body: {“amount”: double, “currency”: string, “type”: string, “parent_id”: integer}
transaction_id: an integer that identifies the new transaction
amount: a double for the amount of the transaction
currency: a string for the currency of the transaction
type: a string for defining what the transaction is for
parent_id: an optional integer which might link to a related parent transaction
GET /bookingservice/transaction/$transaction_id
Returns the specified transaction as JSON.
GET /bookingservice/types/$type
Returns a JSON list of all transaction ids of the given type.
GET /bookingservice/currencies
Returns a JSON list with all used currencies in the existing transactions.
GET /bookingservice/sum/$transaction_id
Returns: {“sum”: double, “currency”: string}
Returns the sum of all linked transactions with the respective currency.
(Note: all linked transactions must have the same currency)
Input / Output Examples:
PUT /bookingservice/transaction/42
{“amount”: 23, “currency”: “EUR”, “type”: “expenses”}
➔ {“status”: “ok”}

PUT /bookingservice/transaction/111
{“amount”: 324, “currency”: “EUR”, “type”: “restaurant”, “parent_id”: 42}
➔ {“status”: “ok”}

PUT /bookingservice/transaction/112
{“amount”: 324, “currency”: “USD”, “type”: “restaurant”, “parent_id”: 42}
➔ {“status”: “failed”}
GET /bookingservice/types/expenses
➔ [42]

GET /bookingservice/currencies
➔ [“EUR”]

GET /bookingservice/sum/42
➔ {“sum”: 347, “currency”: “EUR”}
