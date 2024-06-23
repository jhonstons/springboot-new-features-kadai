 const stripe = Stripe('pk_test_51OWufQCP2Ja3niUVdBZbvCJvgxwk7vCwSWj20bkNK2lseRhR5odBvVucP5wbIZpUtDZoj1iiLEZSYTlA6AdzjiQ700EF3biv31');
 const paymentButton = document.querySelector('#paymentButton');
 
 paymentButton.addEventListener('click', () => {
   stripe.redirectToCheckout({
     sessionId: sessionId
   })
 });