package com.nikhu.ecommerce.shoppingcart

import ddd.cqrs.{CommandId, DomainCommand, DomainEvent, EventId}

/**
  * Created by vsubbarravuri on 8/29/16.
  */

case class CartId(value: String)

//
// Commands
//
sealed trait CartCommand extends DomainCommand

case class CreateCart(cartId: CartId, items: List[CartItem]) extends CartCommand

case class AddToCart(cartId: CartId, item: CartItem) extends CartCommand

case class DeleteCart(cartId: CartId, items: List[CartItem]) extends CartCommand

//
// Events
//

case class CartCreated(commandId: CommandId, cartId: CartId, items: List[CartItem]) extends DomainEvent

case class AddedItem(commandId: CommandId, cartId: CartId, item: CartItem) extends DomainEvent

case class RemovedItem(commandId: CommandId, cartId: CartId, items: List[CartItem]) extends DomainEvent

case class AbandonedCart(commandId: CommandId, cartId: CartId) extends DomainEvent

case class ExpiredCart(commandId: CommandId, cartId: CartId) extends DomainEvent

case class DeletedCart(commandId: CommandId, cartId: CartId) extends DomainEvent

case class CheckoutCart(commandId: CommandId, cartId: CartId) extends DomainEvent


// Value Objects

object CartStatus extends Enumeration {
  type CartStatus = Value
  val Created, ItemAdded, Checkout, Abandoned, Expired, Deleted = Value
}

case class CartItem(product: Product, quantity: Int) {

  def increaseQuantity(addedQuantity: Int) = copy(quantity = this.quantity + addedQuantity)

  def productId = product.id
}

case class Product(id: String, version: Long, name: String, productType: String, price: Double)